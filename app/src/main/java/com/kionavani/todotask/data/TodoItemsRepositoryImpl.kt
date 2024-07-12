package com.kionavani.todotask.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.kionavani.todotask.data.database.AppDatabase
import com.kionavani.todotask.data.remote.NetworkResult
import com.kionavani.todotask.data.remote.TasksService
import com.kionavani.todotask.domain.ToDoItem
import com.kionavani.todotask.domain.TodoItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Имплементация репозитория для работы с данными
 */
class TodoItemsRepositoryImpl(
    private val networkService: TasksService,
    private val database: AppDatabase,
    private val dataStore: DataStore<Preferences>,
    private val tasksMapper: TasksMapper,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher
) : TodoItemsRepository {
    private val _todoItems = MutableStateFlow<List<ToDoItem>>(emptyList())
    override val todoItems: StateFlow<List<ToDoItem>> = _todoItems.asStateFlow()

    private val REVISION_KEY = intPreferencesKey(DataStoreContract.REVISION_KEY)
    private var currentRevision = 0

    private var isOnline = true

    private suspend fun saveCurrentRevision(revision: Int) {
        dataStore.edit { preferences ->
            preferences[REVISION_KEY] = revision
        }
    }

    private val localRevisionFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[REVISION_KEY] ?: 0
        }

    override suspend fun changeNetworkStatus(isOnline: Boolean) {
        this.isOnline = isOnline
    }

    override suspend fun fetchData() {
        withContext(dispatcher) {
            currentRevision = localRevisionFlow.first()
            val localTasks = tasksMapper.fromDatabaseList(database.tasksDao().getAll())
            Log.i("Fetching", "local - $currentRevision")

            if (isOnline && currentRevision == 0 && localTasks.isEmpty()) {
                getFromServerOnly()
                Log.i("Fetching", "Server only")
            } else if (isOnline) {
                fetchAndSyncData(localTasks)
                Log.i("Fetching", "Syncing data")
            } else {
                _todoItems.value = localTasks
                Log.i("Fetching", "Db only")
            }
        }
    }

    private suspend fun getFromServerOnly() {
        when (val response = networkService.getList()) {
            is NetworkResult.Error -> throw response.exception
            is NetworkResult.Success -> {
                val mappedList = tasksMapper.fromNetworkList(response.data)
                currentRevision = response.data.revision

                saveCurrentRevision(currentRevision)
                database.tasksDao().updateTasks(tasksMapper.toDatabaseList(mappedList))
                _todoItems.value = mappedList
            }
        }
    }

    private suspend fun fetchAndSyncData(localTasks: List<ToDoItem>) {
        when (val response = networkService.getList()) {
            is NetworkResult.Error -> throw response.exception
            is NetworkResult.Success -> {
                val serverRevision = response.data.revision
                Log.i("Fetching", "$serverRevision")

                if (serverRevision == currentRevision) {
                    Log.i("Fetching", "Revision not changed")
                    _todoItems.value = localTasks
                    syncLocalChangesWithServer(localTasks)
                } else {
                    val mappedList = tasksMapper.fromNetworkList(response.data)
                    _todoItems.value = mappedList
                    database.tasksDao().updateTasks(tasksMapper.toDatabaseList(mappedList))
                    Log.i("Fetching", "Revisions changed")
                    currentRevision = serverRevision
                    saveCurrentRevision(currentRevision)
                }
            }
        }
    }

    private suspend fun syncLocalChangesWithServer(localTasks: List<ToDoItem>) {
        coroutineScope.async(dispatcher) {
            val mappedTasks = tasksMapper.toRequestList(localTasks)
            val patchResponse = networkService.updateList(mappedTasks, currentRevision)

            when (patchResponse) {
                is NetworkResult.Error -> throw patchResponse.exception
                is NetworkResult.Success -> {
                    val updatedTasks = tasksMapper.fromNetworkList(patchResponse.data)

                    database.tasksDao().updateTasks(tasksMapper.toDatabaseList(updatedTasks))
                    _todoItems.value = updatedTasks

                    currentRevision = patchResponse.data.revision
                    saveCurrentRevision(currentRevision)
                }
            }
        }.await()
    }


    override suspend fun addTodoItem(item: ToDoItem) {
        coroutineScope.async(dispatcher) {
            _todoItems.value += item
            database.tasksDao().insertTask(tasksMapper.toDatabaseElement(item))
            if (isOnline) {
                val response =
                    networkService.addTask(tasksMapper.toRequestElement(item), currentRevision)
                when (response) {
                    is NetworkResult.Error -> throw response.exception
                    is NetworkResult.Success -> currentRevision = response.data.revision
                }
            }
        }.await()
    }

    override suspend fun updateTodoItem(newItem: ToDoItem) {
        coroutineScope.async(dispatcher) {
            val itemToCompare = _todoItems.value.find { it.id == newItem.id }
            if (itemToCompare != null) {
                val itemToAdd = itemToCompare.copy(
                    taskDescription = newItem.taskDescription,
                    importance = newItem.importance,
                    deadlineDate = newItem.deadlineDate,
                    changingDate = newItem.changingDate
                )
                _todoItems.value = _todoItems.value.map {
                    if (it.id == itemToAdd.id) itemToAdd else it
                }

                database.tasksDao().updateTask(tasksMapper.toDatabaseElement(itemToAdd))

                if (isOnline) {
                    val response = networkService.updateTask(
                        tasksMapper.toRequestElement(itemToAdd), currentRevision
                    )

                    when (response) {
                        is NetworkResult.Error -> throw response.exception
                        is NetworkResult.Success -> currentRevision = response.data.revision
                    }
                }
            }
        }.await()
    }

    override suspend fun deleteTodoItem(itemId: String) {
        coroutineScope.async(dispatcher) {
            _todoItems.value = _todoItems.value.filter { it.id != itemId }

            database.tasksDao().deleteById(itemId)

            if (isOnline) {
                val response = networkService.deleteTask(itemId, currentRevision)

                when (response) {
                    is NetworkResult.Error -> throw response.exception
                    is NetworkResult.Success -> currentRevision = response.data.revision
                }
            }
        }.await()
    }

    override suspend fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        coroutineScope.async(dispatcher) {
            var item: ToDoItem? = null
            _todoItems.value = _todoItems.value.map {
                if (it.id == itemId) {
                    item = it.copy(isCompleted = isCompleted)
                    item!!
                } else it
            }
            if (item != null) {
                database.tasksDao().updateTask(tasksMapper.toDatabaseElement(item!!))
                if (isOnline) {
                    val response = networkService.updateTask(
                        tasksMapper.toRequestElement(item!!), currentRevision
                    )

                    when (response) {
                        is NetworkResult.Error -> throw response.exception
                        is NetworkResult.Success -> currentRevision = response.data.revision
                    }
                }
            }
        }.await()
    }

    override suspend fun getTaskById(itemId: String): ToDoItem? = withContext(dispatcher) {
        _todoItems.value.find { it.id == itemId }
    }


    override suspend fun getNextId(): String = withContext(dispatcher) {
        UUID.randomUUID().toString()
    }
}

object DataStoreContract {
    const val STORE_NAME = "todo_tasks_store"
    const val REVISION_KEY = "current_revision"
}
