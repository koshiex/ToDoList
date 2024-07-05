package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.Serializable

sealed class SingleElementDto {
    @Serializable
    data class SingleElementResponseDto(
        val status: String,
        val element: ElementDto,
        val revision: Int
    )

    @Serializable
    data class SingleElementRequestDto(
        val element: ElementDto
    )
}