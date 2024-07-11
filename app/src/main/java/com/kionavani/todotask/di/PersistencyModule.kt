package com.kionavani.todotask.di

import android.content.Context
import androidx.room.Room
import com.kionavani.todotask.data.database.AppDatabase
import com.kionavani.todotask.data.database.Converters
import com.kionavani.todotask.data.database.TasksDbContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistencyModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = TasksDbContract.DATABASE_NAME
        ).addTypeConverter(Converters()).build()
    }
}