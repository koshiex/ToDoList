package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListElementResponseDto(
    val status: String,
    val list: List<ElementDTO>,
    val revision: Int
)