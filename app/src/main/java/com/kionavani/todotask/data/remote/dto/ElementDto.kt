package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO одной таски для работы с сетью
 */
// TODO: сделать все nullable
@Serializable
data class ElementDto(
    @SerialName("id")
    val id: String,
    @SerialName("text")
    val text: String,
    @SerialName("importance")
    val importance: String,
    @SerialName("deadline")
    val deadline: Long? = null,
    @SerialName("done")
    val done: Boolean,
    @SerialName("color")
    val color: String? = null,
    @SerialName("created_at")
    val created_at: Long,
    @SerialName("changed_at")
    val changed_at: Long,
    @SerialName("last_updated_by")
    val last_updated_by: String
)
