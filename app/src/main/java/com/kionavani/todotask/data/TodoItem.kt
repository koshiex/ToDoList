package com.kionavani.todotask.data

/**
 * Модель таски для работы с UI
 */
data class ToDoItem(
    val id: String,
    val taskDescription: String,
    val isCompleted: Boolean,
    val importance: Importance,
    val creatingDate: Long,
    val deadlineDate: Long? = null,
    val changingDate: Long? = null
)


