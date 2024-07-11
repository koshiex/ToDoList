package com.kionavani.todotask.data.database

import androidx.room.TypeConverter
import com.kionavani.todotask.data.Importance
import com.kionavani.todotask.data.toDtoName
import com.kionavani.todotask.data.toImportance

class Converters {
    @TypeConverter
    fun fromImportance(value: Importance) : String {
        return value.toDtoName()
    }

    @TypeConverter
    fun toImportance(value: String) : Importance {
        return value.toImportance()
    }
}