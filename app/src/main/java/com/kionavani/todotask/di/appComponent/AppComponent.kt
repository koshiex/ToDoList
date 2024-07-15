package com.kionavani.todotask.di.appComponent

import android.content.Context
import com.kionavani.todotask.ui.DataFetchWorker
import com.kionavani.todotask.di.AppScope
import com.kionavani.todotask.di.screensComponent.ScreenComponent
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

    fun screenComponent(): ScreenComponent.Factory

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun mainScope(mainScope: CoroutineScope) : Builder
        @BindsInstance
        fun context(application: Context): Builder
    }
}