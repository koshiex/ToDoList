package com.kionavani.todotask.di

import android.content.Context
import com.kionavani.todotask.data.remote.TasksService
import com.kionavani.todotask.data.remote.TasksServiceImpl
import com.kionavani.todotask.data.remote.createHttpClient
import com.kionavani.todotask.domain.NetworkMonitor
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
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