package com.kionavani.todotask

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToDoViewModel(private val repository: TodoItemsRepository) : ViewModel() {
    val todoItems: StateFlow<List<ToDoItem>> = repository.todoItems

    fun addTodoItem(item: ToDoItem) {
        repository.addTodoItem(item)
    }

    fun updateTodoItem(newItem: ToDoItem) {
        repository.updateTodoItem(newItem)
    }

    fun deleteTodoItem(itemId: String) {
        repository.deleteTodoItem(itemId)
    }

    fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        repository.toggleTaskCompletion(itemId, isCompleted)
    }

    fun getNextId(): String = repository.getNextId()
    fun filterTasksByCompleted(tasks: List<ToDoItem>) = tasks.filterNot { it.isCompleted }
    fun getCompletedCount() = todoItems.value.count { it.isCompleted }

    fun dateToString(timestamp: Long): String {
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return formatter.format(date)
    }

    fun getTaskById(itemId: String) = repository.getTaskById(itemId)
}
