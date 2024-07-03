package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ElementDTO(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long? = null,
    val done: Boolean,
    val color: String? = null,
    val created_at: Long,
    val changed_at: Long,
    val last_updated_by: String
)
