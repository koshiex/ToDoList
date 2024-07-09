package com.kionavani.todotask.data.remote

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kionavani.todotask.domain.TodoItemsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Класс Worker'a, отвечающий за синхронизацию данных в фоне
 */
class DataFetchWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TodoItemsRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val response = repository.fetchData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}