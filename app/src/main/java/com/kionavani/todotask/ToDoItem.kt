package com.kionavani.todotask

import androidx.annotation.StringRes
import java.time.LocalDateTime

data class ToDoItem(
    val id: String,
    var taskDescription: String,
    var isCompleted: Boolean,
    var importanceTask: Importance,
    var creatingDate: LocalDateTime,
    var deadlineDate: LocalDateTime? = null,
    var changingDate: LocalDateTime? = null
)


enum class Importance(@StringRes val displayName: Int) {
    LOW(R.string.importance_low),
    REGULAR(R.string.importance_regular),
    HIGH(R.string.importance_high)
}
