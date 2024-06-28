package com.kionavani.todotask.data

import androidx.annotation.StringRes
import com.kionavani.todotask.R

data class ToDoItem(
    val id: String,
    val taskDescription: String,
    val isCompleted: Boolean,
    val importance: Importance,
    val creatingDate: Long,
    val deadlineDate: Long? = null,
    val changingDate: Long? = null
)

enum class Importance(@StringRes val displayName: Int) {
    LOW(R.string.importance_low),
    REGULAR(R.string.importance_regular),
    HIGH(R.string.importance_high)
}
