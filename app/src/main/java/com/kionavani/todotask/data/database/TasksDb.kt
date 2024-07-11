package com.kionavani.todotask.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kionavani.todotask.data.Importance

@Entity(tableName = TasksDbContract.TasksDb.TABLE_NAME)
class TasksDb(
    @PrimaryKey
    @ColumnInfo(name = TasksDbContract.TasksDb.COLUMN_NAME_ID)
    val id: String,
    @ColumnInfo(name = TasksDbContract.TasksDb.COLUMN_NAME_DESCRIPTION)
    val description: String,
    @ColumnInfo(name = TasksDbContract.TasksDb.COLUMN_NAME_COMPLETED)
    val isCompleted: Boolean,
    @ColumnInfo(name = TasksDbContract.TasksDb.COLUMN_NAME_IMPORTANCE)
    val importance: Importance,
    @ColumnInfo(name = TasksDbContract.TasksDb.COLUMN_NAME_CREATING_DATE)
    val creatingDate: Long,
    @ColumnInfo(name = TasksDbContract.TasksDb.COLUMN_NAME_DEADLINE_DATE)
    val deadlineDate: Long? = null,
    @ColumnInfo(name = TasksDbContract.TasksDb.COLUMN_NAME_CHANGING_DATE)
    val changingDate: Long? = null
)