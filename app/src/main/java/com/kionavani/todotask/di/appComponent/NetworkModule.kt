package com.kionavani.todotask.di.appComponent

import android.content.Context
import com.kionavani.todotask.data.remote.Endpoints
import com.kionavani.todotask.data.remote.TasksService
import com.kionavani.todotask.data.remote.TasksServiceImpl
import com.kionavani.todotask.data.remote.createHttpClient
import com.kionavani.todotask.ui.NetworkMonitor
import com.kionavani.todotask.di.AppScope
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

/**
 * Модуль для инжектирования всей сетевой части
 */
@Module
class NetworkModule {
    @Provides
    @AppScope
    fun provideHttpClient(): HttpClient {
        return createHttpClient(OkHttp.create())
    }


    @Provides
    @AppScope
    fun provideTasksService(client: HttpClient): TasksService {
        return TasksServiceImpl(client, Endpoints.BASE_URL)
    }

    @Provides
    @AppScope
    fun provideNetworkMonitor(context: Context) : NetworkMonitor {
        return NetworkMonitor(context)
    }
}