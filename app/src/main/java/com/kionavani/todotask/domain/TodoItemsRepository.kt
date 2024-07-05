package com.kionavani.todotask.domain

import com.kionavani.todotask.data.ToDoItem
import kotlinx.coroutines.flow.StateFlow

interface TodoItemsRepository {
    val todoItems: StateFlow<List<ToDoItem>>

    suspend fun fetchData()

    suspend fun addTodoItem(item: ToDoItem)

    suspend fun updateTodoItem(newItem: ToDoItem)

    suspend fun deleteTodoItem(itemId: String)

    suspend fun toggleTaskCompletion(itemId: String, isCompleted: Boolean)

    suspend fun getTaskById(itemId: String): ToDoItem?

    suspend fun getNextId(): String
}
