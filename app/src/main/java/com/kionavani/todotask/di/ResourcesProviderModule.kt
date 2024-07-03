package com.kionavani.todotask.di

import android.content.Context
import com.kionavani.todotask.ui.ResourcesProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ResourcesProviderModule {
    @Provides
    @Singleton
    fun provideResourcesProvider(context: Context) : ResourcesProvider {
        return ResourcesProvider(context)
    }
}