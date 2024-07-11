package com.kionavani.todotask.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.kionavani.todotask.data.ToDoItem
import kotlinx.coroutines.flow.Flow
import okhttp3.internal.concurrent.Task

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(tasksDb: TasksDb)

    @Update
    suspend fun updateTask(tasksDb: TasksDb)

    @Upsert
    suspend fun upsertTask(tasksDb: TasksDb)

    @Delete
    suspend fun deleteTask(tasksDb: TasksDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasksList(tasks: List<TasksDb>)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM ${TasksDbContract.TasksDb.TABLE_NAME}")
    suspend fun getAll(): List<TasksDb>

    @Query("SELECT * FROM ${TasksDbContract.TasksDb.TABLE_NAME}")
    suspend fun observeAll(): Flow<List<TasksDb>>

    @Query("DELETE FROM tasks WHERE ${TasksDbContract.TasksDb.COLUMN_NAME_ID} = :taskId")
    suspend fun deleteById(taskId: String)

    @Transaction
    suspend fun updateTasks(tasks: List<TasksDb>) {
        deleteAllTasks()
        insertTasksList(tasks)
    }
}