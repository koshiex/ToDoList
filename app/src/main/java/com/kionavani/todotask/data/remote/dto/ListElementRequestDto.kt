package com.kionavani.todotask.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ListElementRequestDto(
    val list: List<ElementDTO>
)
