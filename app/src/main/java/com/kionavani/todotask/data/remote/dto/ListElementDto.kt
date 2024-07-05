package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.Serializable


sealed class ListElementDto {
    @Serializable
    data class ListElementResponseDto(
        val status: String,
        val list: List<ElementDto>,
        val revision: Int
    )

    @Serializable
    data class ListElementRequestDto(
        val list: List<ElementDto>
    )
}

