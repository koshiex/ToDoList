package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed класс с DTO ответами для работы с сетью
 */
sealed class ResponseDto {
    /**
     * DTO ответа с списоком тасок
     */
    @Serializable
    data class ListElementResponseDto(
        @SerialName("status")
        val status: String,
        @SerialName("list")
        val list: List<ElementDto>,
        @SerialName("revision")
        val revision: Int
    )
    /**
     * DTO ответа с одной таской
     */
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

