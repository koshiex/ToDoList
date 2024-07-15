package com.kionavani.todotask.ui

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kionavani.todotask.data.DataStoreContract
import com.kionavani.todotask.di.appComponent.AppComponent
import com.kionavani.todotask.di.appComponent.DaggerAppComponent
import com.kionavani.todotask.di.screensComponent.ScreenComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import java.util.concurrent.TimeUnit

/**
 * Класс приложения... приложения
 */
val Context.dataStore: DataStore<Preferences>
    by preferencesDataStore(name = DataStoreContract.STORE_NAME)


class TodoApplication : Application() {
    val appComponent: AppComponent by lazy { setupDi() }
    val screenComponent: ScreenComponent by lazy { appComponent.screenComponent().create() }
    private val mainScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob()) }

    private fun setupDi() = DaggerAppComponent.builder()
        .context(this)
        .mainScope(mainScope)
        .build()

    override fun onCreate() {
        super.onCreate()
        setupWorker()
    }

    private fun setupWorker() {
        val fetchWork = PeriodicWorkRequestBuilder<DataFetchWorker>(8, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DataFetchWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            fetchWork
        )
    }
}