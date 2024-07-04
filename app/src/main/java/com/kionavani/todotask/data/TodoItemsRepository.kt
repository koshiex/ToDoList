package com.kionavani.todotask.data

import com.kionavani.todotask.data.remote.NetworkResult
import com.kionavani.todotask.data.remote.TasksService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TodoItemsRepository(
    private val networkService: TasksService,
    private val tasksMapper: TasksMapper
) {
    private val _todoItems = MutableStateFlow<List<ToDoItem>>(emptyList())
    val todoItems: StateFlow<List<ToDoItem>> = _todoItems.asStateFlow()

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var currentRevision = 0

    init {
        repositoryScope.launch {
            when(val response = networkService.getList()) {
                is NetworkResult.Error -> throw response.exception
                is NetworkResult.Success -> {
                    val mappedList = tasksMapper.toEntityList(response.data)
                    currentRevision = response.data.revision
                    _todoItems.emit(mappedList)
                }
            }
        }
    }

    fun addTodoItem(item: ToDoItem) {
        repositoryScope.launch {
            val currentList = _todoItems.first()
            _todoItems.emit(currentList + item)
        }
    }

    fun updateTodoItem(newItem: ToDoItem) {
        repositoryScope.launch {
            val currentList = _todoItems.first()
            val itemToCompare = currentList.find { it.id == newItem.id }
            if (itemToCompare != null) {
                val itemToAdd = itemToCompare.copy(
                    taskDescription = newItem.taskDescription,
                    importance = newItem.importance,
                    deadlineDate = newItem.deadlineDate,
                    changingDate = newItem.changingDate
                )
                _todoItems.emit(currentList.map {
                    if (it.id == itemToAdd.id) itemToAdd else it
                })
            }
        }
    }

    fun deleteTodoItem(itemId: String) {
        repositoryScope.launch {
            val currentList = _todoItems.first()
            _todoItems.emit(currentList.filter { it.id != itemId })
        }
    }

    fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        repositoryScope.launch {
            val currentList = _todoItems.first()
            _todoItems.emit(currentList.map { item ->
                if (item.id == itemId) item.copy(isCompleted = isCompleted) else item
            })
        }
    }

    fun getTaskById(itemId: String) = _todoItems.value.find { it.id == itemId }

    fun getNextId(): String {
        return if (_todoItems.value.isNotEmpty()) {
            (_todoItems.value.last().id.toInt() + 1).toString()
        } else {
            "0"
        }
    }
}
