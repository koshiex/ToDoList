package com.kionavani.todotask.di

import android.app.Application
import android.content.Context
import com.kionavani.todotask.domain.MainActivity
import com.kionavani.todotask.ui.ResourcesProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, ResourcesProviderModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun context(application: Context): Builder
    }
}