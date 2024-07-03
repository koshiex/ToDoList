package com.kionavani.todotask.domain

import android.app.Application
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.di.AppComponent
import com.kionavani.todotask.di.DaggerAppComponent
import com.kionavani.todotask.di.NetworkModule
import com.kionavani.todotask.di.RepositoryModule
import com.kionavani.todotask.di.ResourcesProviderModule
import com.kionavani.todotask.ui.ResourcesProvider

class TodoApplication : Application() {
    val appComponent: AppComponent by lazy { setupDi() }

    private fun setupDi() = DaggerAppComponent.builder()
        .context(this)
        .build()
}