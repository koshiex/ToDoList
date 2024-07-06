package com.kionavani.todotask.data.remote

import android.util.Log
import com.kionavani.todotask.data.remote.dto.ResponseDto.*
import com.kionavani.todotask.data.remote.dto.RequestDto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import com.kionavani.todotask.BuildConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.logging.Logger
import io.ktor.http.ContentType.Application
import io.ktor.http.HttpHeaders.ContentType
import kotlinx.serialization.json.Json

private const val REVISION_HEADER = "X-Last-Known-Revision"
private const val CLIENT_LOGGER_TAG = "HTTP call"

fun createHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }

    install(HttpRequestRetry) {
        retryOnException(maxRetries = 3)
        retryOnServerErrors(maxRetries = 3)
        exponentialDelay()
    }

    // TODO: Я.Паспорт
    defaultRequest {
        header("Authorization", "OAuth ${BuildConfig.OAUTH_TOKEN}")
        header(ContentType, Application.Json)
    }

    if (BuildConfig.DEBUG) {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d(CLIENT_LOGGER_TAG, message)
                }
            }
            level = LogLevel.ALL
        }
    }
}

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
            header(REVISION_HEADER, revision.toString())
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
            val response: SingleElementResponseDto = client.post {
                url(Endpoints.LIST)
                header(REVISION_HEADER, revision.toString())
                setBody(task)
            }.body()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }

    override suspend fun updateTask(task: SingleElementRequestDto, revision: Int): NetworkResult<SingleElementResponseDto> =
        try {
            val taskId = task.element.id
            val response: SingleElementResponseDto = client.put {
                url(Endpoints.getListOneUrl(taskId))
                header(REVISION_HEADER, revision.toString())
                setBody(task)
            }.body()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }

    override suspend fun deleteTask(taskId: String, revision: Int): NetworkResult<SingleElementResponseDto> =
        try {
            val response: SingleElementResponseDto = client.delete {
                url(Endpoints.getListOneUrl(taskId))
                header(REVISION_HEADER, revision.toString())
            }.body()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }

}