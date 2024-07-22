package com.kionavani.todotask.di.appComponent

import android.content.Context
import com.kionavani.todotask.ui.DataFetchWorker
import com.kionavani.todotask.di.AppScope
import com.kionavani.todotask.di.screensComponent.ScreenComponent
import com.kionavani.todotask.ui.TodoApplication
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope

/**
 * Главный компонент приложения для инжектирования
 */
@AppScope
@Component(modules = [
    NetworkModule::class,
    RepositoryModule::class,
    ResourcesProviderModule::class,
    PersistencyModule::class
])
interface AppComponent {
    fun inject(worker: DataFetchWorker)

    fun screenComponent(): ScreenComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun mainScope(mainScope: CoroutineScope) : Builder
        @BindsInstance
        fun context(application: Context): Builder
        fun build(): AppComponent

    }
}