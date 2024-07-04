package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.Serializable


sealed class ListElementDto {
    @Serializable
    data class ListElementResponseDto(
        val status: String,
        val list: List<ElementDTO>,
        val revision: Int
    )

    @Serializable
    data class ListElementRequestDto(
        val list: List<ElementDTO>
    )
}

