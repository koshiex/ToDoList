package com.kionavani.todotask.data.remote

import com.kionavani.todotask.data.remote.dto.ListElementDto.*
import com.kionavani.todotask.data.remote.dto.SingleElementDto.*

interface TasksService {
    suspend fun getList(): NetworkResult<ListElementResponseDto>
    suspend fun updateList(
        list: ListElementRequestDto,
        revision: Int
    ): NetworkResult<ListElementResponseDto>

    suspend fun getTask(taskId: String): NetworkResult<SingleElementResponseDto>
    suspend fun addTask(
        task: SingleElementRequestDto,
        revision: Int
    ): NetworkResult<SingleElementResponseDto>

    suspend fun updateTask(task: SingleElementRequestDto): NetworkResult<SingleElementResponseDto>
    suspend fun deleteTask(taskId: String): NetworkResult<SingleElementResponseDto>
}
