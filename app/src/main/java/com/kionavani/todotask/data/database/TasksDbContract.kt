package com.kionavani.todotask.data.database

object TasksDbContract {
    const val DATABASE_NAME = "TasksDb.db"

    object TasksDb {
        const val TABLE_NAME = "Tasks"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_COMPLETED = "isCompleted"
        const val COLUMN_NAME_IMPORTANCE = "importance"
        const val COLUMN_NAME_CREATING_DATE = "creatingDate"
        const val COLUMN_NAME_DEADLINE_DATE = "deadlineDate"
        const val COLUMN_NAME_CHANGING_DATE = "changingDate"
    }
}