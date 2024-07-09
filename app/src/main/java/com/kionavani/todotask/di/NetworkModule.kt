package com.kionavani.todotask.di

import android.content.Context
import com.kionavani.todotask.data.remote.TasksService
import com.kionavani.todotask.data.remote.TasksServiceImpl
import com.kionavani.todotask.data.remote.createHttpClient
import com.kionavani.todotask.data.NetworkMonitor
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import javax.inject.Singleton

/**
 * Модуль для инжектирования всей сетевой части
 */
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return createHttpClient()
    }


    @Provides
    @Singleton
    fun provideTasksService(client: HttpClient): TasksService {
        return TasksServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(context: Context) : NetworkMonitor {
        return NetworkMonitor(context)
    }
}