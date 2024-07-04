package com.kionavani.todotask.di

import com.kionavani.todotask.data.TasksMapper
import com.kionavani.todotask.data.TodoItemsRepository
import com.kionavani.todotask.data.remote.TasksService
import com.kionavani.todotask.data.remote.TasksServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideTasksRepository(tasksService: TasksService, tasksMapper: TasksMapper): TodoItemsRepository {
        return TodoItemsRepository(tasksService, tasksMapper)
    }

    @Provides
    @Singleton
    fun provideTasksMapper() : TasksMapper {
        return TasksMapper()
    }
}