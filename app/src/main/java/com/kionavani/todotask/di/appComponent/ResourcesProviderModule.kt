package com.kionavani.todotask.di.appComponent

import android.content.Context
import com.kionavani.todotask.di.AppScope
import com.kionavani.todotask.ui.ResourcesProvider
import dagger.Module
import dagger.Provides

/**
 * Модуль для инжектирования ResourcesProvider-а
 */
@Module
class ResourcesProviderModule {
    @Provides
    @AppScope
    fun provideResourcesProvider(context: Context) : ResourcesProvider {
        return ResourcesProvider(context)
    }
}