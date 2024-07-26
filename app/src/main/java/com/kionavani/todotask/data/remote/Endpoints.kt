package com.kionavani.todotask.data.remote

import com.kionavani.todotask.BuildConfig

/**
 * Объект с константными эндпоинтами для работы с сетью
 */
object Endpoints {
    const val BASE_URL = BuildConfig.BASE_URL
    const val LIST = "/list"

    fun getListOneUrl(id: String, baseUrl: String): String {
        return "$baseUrl/list/$id"
    }
}