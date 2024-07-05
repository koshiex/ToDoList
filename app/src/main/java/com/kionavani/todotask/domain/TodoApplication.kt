package com.kionavani.todotask.domain

import android.app.Application
import com.kionavani.todotask.di.AppComponent
import com.kionavani.todotask.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TodoApplication : Application() {
    val appComponent: AppComponent by lazy { setupDi() }
    private val mainScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob()) }

    private fun setupDi() = DaggerAppComponent.builder()
        .context(this)
        .mainScope(mainScope)
        .build()
}