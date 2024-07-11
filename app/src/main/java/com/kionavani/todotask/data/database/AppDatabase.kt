package com.kionavani.todotask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [TasksDb::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasksDao() : TasksDao
}