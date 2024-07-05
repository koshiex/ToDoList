package com.kionavani.todotask.data

import com.kionavani.todotask.data.remote.NetworkResult
import com.kionavani.todotask.data.remote.TasksService
import com.kionavani.todotask.domain.TodoItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoItemsRepositoryImpl(
    private val networkService: TasksService,
    private val tasksMapper: TasksMapper,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher
) : TodoItemsRepository{
    private val _todoItems = MutableStateFlow<List<ToDoItem>>(emptyList())
    override val todoItems: StateFlow<List<ToDoItem>> = _todoItems.asStateFlow()

    private var currentRevision = 0

    override suspend fun fetchData() {
        withContext(dispatcher) {
            when(val response = networkService.getList()) {
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
        coroutineScope.launch(dispatcher) {
            _todoItems.value += item
        }.join()
    }

    override suspend fun updateTodoItem(newItem: ToDoItem) {
        coroutineScope.launch(dispatcher) {
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
            }
        }.join()
    }

    override suspend fun deleteTodoItem(itemId: String) {
        coroutineScope.launch(dispatcher) {
            _todoItems.value = _todoItems.value.filter { it.id != itemId }
        }.join()
    }

    override suspend fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        coroutineScope.launch(dispatcher) {
            _todoItems.value = _todoItems.value.map { item ->
                if (item.id == itemId) item.copy(isCompleted = isCompleted) else item
            }
        }.join()
    }

    override suspend fun getTaskById(itemId: String) : ToDoItem? {
        val result: ToDoItem?
        withContext(dispatcher) {
            result = _todoItems.value.find { it.id == itemId }
        }
        return result
    }

    override suspend fun getNextId(): String {
        val res: String
        withContext(dispatcher) {
            res = if (_todoItems.value.isNotEmpty()) {
                (_todoItems.value.last().id.toInt() + 1).toString()
            } else {
                "0"
            }
        }
        return res
    }
}
