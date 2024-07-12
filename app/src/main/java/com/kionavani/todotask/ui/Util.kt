package com.kionavani.todotask.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Утилиный объект для различных частых и общих операций по типу форматирования даты
 */
object Util {
    fun dateToString(timestamp: Long): String {
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return formatter.format(date)
    }
}