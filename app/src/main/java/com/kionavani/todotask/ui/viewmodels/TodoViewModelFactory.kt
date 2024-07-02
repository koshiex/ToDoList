package com.kionavani.todotask.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.ui.ResourcesProvider

class TodoViewModelFactory(
    private val repository: TodoItemsRepository,
    private val provider: ResourcesProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository, provider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
