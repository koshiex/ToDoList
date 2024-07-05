package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class RequestDto {
    @Serializable
    data class SingleElementRequestDto(
        @SerialName("element")
        val element: ElementDto
    )

    @Serializable
    data class ListElementRequestDto(
        @SerialName("list")
        val list: List<ElementDto>
    )
}