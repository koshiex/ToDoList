package com.kionavani.todotask.domain

import android.app.Application
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.ui.ResourcesProvider

class TodoApplication : Application() {
    val resourcesProvider by lazy { ResourcesProvider(this) }
    val todoItemsRepository by lazy { TodoItemsRepository() }

}