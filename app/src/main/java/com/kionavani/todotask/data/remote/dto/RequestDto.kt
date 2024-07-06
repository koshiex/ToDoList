package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sealed класс с DTO запросами для работы с сетью
 */
sealed class RequestDto {
    /**
     * DTO запроса с одной таской
     */
    @Serializable
    data class SingleElementRequestDto(
        @SerialName("element")
        val element: ElementDto
    )
    /**
     * DTO запроса с списком тасок
     */
    @Serializable
    data class ListElementRequestDto(
        @SerialName("list")
        val list: List<ElementDto>
    )
}