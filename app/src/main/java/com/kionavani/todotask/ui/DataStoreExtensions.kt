package com.kionavani.todotask.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

suspend fun DataStore<Preferences>.saveNullableBoolean(key: Preferences.Key<Boolean>, value: Boolean?) {
    this.edit { preferences ->
        if (value != null) {
            preferences[key] = value
        } else {
            preferences.remove(key)
        }
    }
}

fun DataStore<Preferences>.getNullableBoolean(key: Preferences.Key<Boolean>): Flow<Boolean?> {
    return this.data.map { preferences ->
        preferences[key]
    }
}