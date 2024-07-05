package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


sealed class ResponseDto {
    @Serializable
    data class ListElementResponseDto(
        @SerialName("status")
        val status: String,
        @SerialName("list")
        val list: List<ElementDto>,
        @SerialName("revision")
        val revision: Int
    )

    @Serializable
    data class SingleElementResponseDto(
        @SerialName("status")
        val status: String,
        @SerialName("element")
        val element: ElementDto,
        @SerialName("revision")
        val revision: Int
    )
}

