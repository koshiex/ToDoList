package com.kionavani.todotask.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kionavani.todotask.R
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.data.TodoItemsRepositoryImpl
import com.kionavani.todotask.domain.TodoItemsRepository
import com.kionavani.todotask.ui.ResourcesProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// TODO : отдельная модель для второго экрана
class MainScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val provider: ResourcesProvider,
) : ViewModel() {
    val todoItems: StateFlow<List<ToDoItem>> = repository.todoItems

    private val _errorFlow = MutableSharedFlow<Exception?>()
    val errorFlow: SharedFlow<Exception?> = _errorFlow.asSharedFlow()
    

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _errorFlow.emit(throwable as? Exception)
        }
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            repository.fetchData()
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
        var res = "0"
        viewModelScope.launch(exceptionHandler) {
            res = repository.getNextId()
        }
        return res
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
        var res: ToDoItem? = null
        viewModelScope.launch(exceptionHandler) {
            res = repository.getTaskById(itemId)
        }
        return res
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
