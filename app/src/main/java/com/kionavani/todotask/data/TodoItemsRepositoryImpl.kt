package com.kionavani.todotask.data

import android.util.Log
import com.kionavani.todotask.data.remote.NetworkResult
import com.kionavani.todotask.data.remote.TasksService
import com.kionavani.todotask.data.remote.dto.ResponseDto
import com.kionavani.todotask.domain.TodoItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Имплементация репозитория для работы с данными
 */
class TodoItemsRepositoryImpl(
    private val networkService: TasksService,
    private val tasksMapper: TasksMapper,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher
) : TodoItemsRepository {
    private val _todoItems = MutableStateFlow<List<ToDoItem>>(emptyList())
    override val todoItems: StateFlow<List<ToDoItem>> = _todoItems.asStateFlow()

    private var currentRevision = 0
    override suspend fun fetchData() {
        withContext(dispatcher) {
            when (val response = networkService.getList()) {
                is NetworkResult.Error -> throw response.exception
                is NetworkResult.Success -> {
                    val mappedList = tasksMapper.toEntityList(response.data)
                    currentRevision = response.data.revision
                    _todoItems.value = mappedList
                }
            }
        }
    }

    override suspend fun addTodoItem(item: ToDoItem) {
        coroutineScope.async(dispatcher) {
            _todoItems.value += item
            val response =
                networkService.addTask(tasksMapper.toRequestElement(item), currentRevision)
            when (response) {
                is NetworkResult.Error -> throw response.exception
                is NetworkResult.Success -> currentRevision = response.data.revision
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
                val response = networkService.updateTask(
                    tasksMapper.toRequestElement(itemToAdd), currentRevision
                )

                when (response) {
                    is NetworkResult.Error -> throw response.exception
                    is NetworkResult.Success -> currentRevision = response.data.revision
                }
            }
        }.await()
    }

    override suspend fun deleteTodoItem(itemId: String) {
        coroutineScope.async(dispatcher) {
            _todoItems.value = _todoItems.value.filter { it.id != itemId }
            val response = networkService.deleteTask(itemId, currentRevision)

            when (response) {
                is NetworkResult.Error -> throw response.exception
                is NetworkResult.Success -> currentRevision = response.data.revision
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
                val response = networkService.updateTask(
                    tasksMapper.toRequestElement(item!!), currentRevision
                )

                when (response) {
                    is NetworkResult.Error -> throw response.exception
                    is NetworkResult.Success -> currentRevision = response.data.revision
                }
            }
        }.await()
    }

    override suspend fun getTaskById(itemId: String): ToDoItem? =
        withContext(dispatcher) {
             _todoItems.value.find { it.id == itemId }
        }


    override suspend fun getNextId(): String =
        withContext(dispatcher) {
            if (_todoItems.value.isNotEmpty()) {
                (_todoItems.value.last().id.toInt() + 1).toString()
            } else {
                "0"
            }
        }

}
