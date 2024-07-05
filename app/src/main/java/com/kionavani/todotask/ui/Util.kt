package com.kionavani.todotask.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {
    fun dateToString(timestamp: Long): String {
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return formatter.format(date)
    }
}