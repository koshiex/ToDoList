package com.kionavani.todotask.data.remote

import com.kionavani.todotask.BuildConfig

object Endpoints {
    private const val BASE_URL = BuildConfig.BASE_URL
    const val LIST = "$BASE_URL/list"

    fun getListOneUrl(id: String): String {
        return "$BASE_URL/list/$id"
    }
}