package com.kionavani.todotask.ui

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kionavani.todotask.domain.TodoItemsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Класс Worker'a, отвечающий за синхронизацию данных в фоне
 */
class DataFetchWorker (
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    @Inject
    lateinit var repository: TodoItemsRepository
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            networkMonitor.startMonitoring()
            val haveConnection = networkMonitor.isConnected.first()
            networkMonitor.stopMonitoring()

            repository.changeNetworkStatus(haveConnection)
            repository.fetchData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}