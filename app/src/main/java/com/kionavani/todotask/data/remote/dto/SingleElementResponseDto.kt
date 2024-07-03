package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SingleElementResponseDto(
    val status: String,
    val element: ElementDTO,
    val revision: Int
)