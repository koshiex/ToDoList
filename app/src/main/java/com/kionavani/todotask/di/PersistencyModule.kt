package com.kionavani.todotask.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.kionavani.todotask.data.database.AppDatabase
import com.kionavani.todotask.data.database.Converters
import com.kionavani.todotask.data.database.TasksDbContract
import com.kionavani.todotask.ui.dataStore
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
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataStore(context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}