package com.kionavani.todotask.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kionavani.todotask.data.DataStoreContract
import com.kionavani.todotask.data.remote.DataFetchWorker
import com.kionavani.todotask.ui.MainActivity
import com.kionavani.todotask.ui.dataStore
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

/**
 * Главный компонент приложения для инжектирования
 */
@Singleton
@Component(modules = [
    NetworkModule::class,
    RepositoryModule::class,
    ResourcesProviderModule::class,
    ViewModelModule::class,
    PersistencyModule::class
])
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(worker: DataFetchWorker)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun mainScope(mainScope: CoroutineScope) : Builder
        @BindsInstance
        fun context(application: Context): Builder
    }
}