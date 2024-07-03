package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SingleElementRequestDto(
    val element: ElementDTO
)
