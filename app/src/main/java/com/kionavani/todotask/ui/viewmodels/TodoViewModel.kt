package com.kionavani.todotask.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.ui.ResourcesProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// TODO : отдельная модель для второго экрана
class TodoViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val provider: ResourcesProvider
) : ViewModel() {
    val todoItems: StateFlow<List<ToDoItem>> = repository.todoItems

    private val _errorFlow = MutableSharedFlow<Exception?>()
    val errorFlow: SharedFlow<Exception?> = _errorFlow.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _errorFlow.emit(throwable as? Exception)
        }
    }

    fun addTodoItem(item: ToDoItem) {
        viewModelScope.launch(exceptionHandler) {
            repository.addTodoItem(item)
        }
    }

    fun updateTodoItem(newItem: ToDoItem) {
        viewModelScope.launch(exceptionHandler) {
            repository.updateTodoItem(newItem)
        }
    }

    fun deleteTodoItem(itemId: String) {
        viewModelScope.launch(exceptionHandler) {
            repository.deleteTodoItem(itemId)
        }
    }

    fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            repository.toggleTaskCompletion(itemId, isCompleted)
        }
    }

    fun getNextId(): String {
        return repository.getNextId()
    }

    fun filterTasksByCompleted(tasks: List<ToDoItem>): List<ToDoItem> {
        return tasks.filterNot { it.isCompleted }
    }

    fun getCompletedCount(): Int {
        return todoItems.value.count { it.isCompleted }
    }

    fun dateToString(timestamp: Long): String {
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return formatter.format(date)
    }

    fun getTaskById(itemId: String): ToDoItem? {
        return repository.getTaskById(itemId)
    }

    fun getDescWithEmoji(item: ToDoItem): String {
        return when (item.importance) {
            Importance.LOW ->
                provider.getString(R.string.low_importance_emoji) + " ${item.taskDescription}"

            Importance.REGULAR ->
                item.taskDescription

            Importance.HIGH ->
                provider.getString(R.string.high_importance_emoji) + " ${item.taskDescription}"
        }
    }
}
