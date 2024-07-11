package com.kionavani.todotask.data.remote

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kionavani.todotask.data.NetworkMonitor
import com.kionavani.todotask.domain.TodoItemsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Класс Worker'a, отвечающий за синхронизацию данных в фоне
 */
class DataFetchWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TodoItemsRepository,
    private val networkMonitor: NetworkMonitor
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            networkMonitor.startMonitoring()
            val haveConnection = networkMonitor.isConnected.first()
            networkMonitor.stopMonitoring()

            repository.fetchData(haveConnection)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}