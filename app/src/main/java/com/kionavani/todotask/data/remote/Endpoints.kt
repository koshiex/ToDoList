package com.kionavani.todotask.data.remote

import com.kionavani.todotask.BuildConfig

/**
 * Объект с константными эндпоинтами для работы с сетью
 */
object Endpoints {
    private val BASE_URL = BuildConfig.BASE_URL
    val LIST = "$BASE_URL/list"

    fun getListOneUrl(id: String): String {
        return "$BASE_URL/list/$id"
    }
}