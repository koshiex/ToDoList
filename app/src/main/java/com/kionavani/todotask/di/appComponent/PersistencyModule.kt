package com.kionavani.todotask.di.appComponent

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.kionavani.todotask.data.database.AppDatabase
import com.kionavani.todotask.data.database.TasksDbContract
import com.kionavani.todotask.di.AppScope
import com.kionavani.todotask.ui.dataStore
import dagger.Module
import dagger.Provides

@Module
class PersistencyModule {
    @Provides
    @AppScope
    fun provideRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = TasksDbContract.DATABASE_NAME
        ).build()
    }

    @Provides
    @AppScope
    fun provideDataStore(context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}