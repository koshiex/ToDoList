package com.kionavani.todotask.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kionavani.todotask.data.ToDoItem
import com.kionavani.todotask.data.TodoItemsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToDoViewModel(private val repository: TodoItemsRepository) : ViewModel() {
    val todoItems: StateFlow<List<ToDoItem>> = repository.todoItems

    fun addTodoItem(item: ToDoItem) {
        viewModelScope.launch {
            repository.addTodoItem(item)
        }
    }

    fun updateTodoItem(newItem: ToDoItem) {
        viewModelScope.launch {
            repository.updateTodoItem(newItem)
        }
    }

    fun deleteTodoItem(itemId: String) {
        viewModelScope.launch {
            repository.deleteTodoItem(itemId)
        }
    }

    fun toggleTaskCompletion(itemId: String, isCompleted: Boolean) {
        viewModelScope.launch {
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
}
