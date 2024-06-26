package com.kionavani.todotask

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

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
}
