package com.kionavani.todotask.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kionavani.todotask.data.TasksMapper
import com.kionavani.todotask.data.TodoItemsRepositoryImpl
import com.kionavani.todotask.data.database.AppDatabase
import com.kionavani.todotask.data.remote.TasksService
import com.kionavani.todotask.domain.TodoItemsRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Модуль для инжектирования репозитория и зависимостей в него
 */
@Module
class RepositoryModule() {
    @Provides
    @Singleton
    fun provideTasksRepository(
        tasksService: TasksService,
        database: AppDatabase,
        dataStore: DataStore<Preferences>,
        tasksMapper: TasksMapper,
        mainScope: CoroutineScope
    ): TodoItemsRepository {
        return TodoItemsRepositoryImpl(
            tasksService, database, dataStore, tasksMapper, mainScope, Dispatchers.IO
        )
    }

    @Provides
    @Singleton
    fun provideTasksMapper(): TasksMapper {
        return TasksMapper()
    }
}