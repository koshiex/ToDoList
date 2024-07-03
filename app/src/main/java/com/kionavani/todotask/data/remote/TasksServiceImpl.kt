package com.kionavani.todotask.data.remote

import com.kionavani.todotask.data.remote.dto.ListElementRequestDto
import com.kionavani.todotask.data.remote.dto.ListElementResponseDto
import com.kionavani.todotask.data.remote.dto.SingleElementRequestDto
import com.kionavani.todotask.data.remote.dto.SingleElementResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url

private const val REVISION_HEADER = "X-Last-Known-Revision"

class TasksServiceImpl(
    private val client: HttpClient
) : TasksService {
    override suspend fun getList(): NetworkResult<ListElementResponseDto> = try {
        val response: ListElementResponseDto = client.get { url(Endpoints.LIST) }.body()
        NetworkResult.Success(response)
    } catch (e: Exception) {
        NetworkResult.Error(e)
    }


    override suspend fun updateList(
        list: ListElementRequestDto, revision: Int
    ): NetworkResult<ListElementResponseDto> = try {
        val response: ListElementResponseDto = client.patch {
            url(Endpoints.LIST)
            setBody(list)
            header(REVISION_HEADER, revision)
        }.body()
        NetworkResult.Success(response)
    } catch (e: Exception) {
        NetworkResult.Error(e)
    }


    override suspend fun getTask(taskId: String): NetworkResult<SingleElementResponseDto> = try {
        val response: SingleElementResponseDto = client.get {
            url(Endpoints.getListOneUrl(taskId))
        }.body()
        NetworkResult.Success(response)
    } catch (e: Exception) {
        NetworkResult.Error(e)
    }


    override suspend fun addTask(
        task: SingleElementRequestDto, revision: Int
    ): NetworkResult<SingleElementResponseDto> =
        try {
            val taskId = task.element.id
            val response: SingleElementResponseDto = client.post {
                url(Endpoints.getListOneUrl(taskId))
                header(REVISION_HEADER, revision)
            }.body()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }

    override suspend fun updateTask(task: SingleElementRequestDto): NetworkResult<SingleElementResponseDto> =
        try {
            val taskId = task.element.id
            val response: SingleElementResponseDto = client.put {
                url(Endpoints.getListOneUrl(taskId))
            }.body()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }

    override suspend fun deleteTask(taskId: String): NetworkResult<SingleElementResponseDto> =
        try {
            val response: SingleElementResponseDto = client.delete {
                url(Endpoints.getListOneUrl(taskId))
            }.body()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }

}