package com.kionavani.todotask.ui

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kionavani.todotask.data.DataStoreContract
import com.kionavani.todotask.di.AppComponent
import com.kionavani.todotask.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Класс приложения... приложения
 */
val Context.dataStore: DataStore<Preferences>
    by preferencesDataStore(name = DataStoreContract.STORE_NAME)

class TodoApplication : Application() {
    val appComponent: AppComponent by lazy { setupDi() }
    private val mainScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob()) }

    private fun setupDi() = DaggerAppComponent.builder()
        .context(this)
        .mainScope(mainScope)
        .build()
}