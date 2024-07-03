package com.kionavani.todotask.data.remote

object Endpoints {
    private const val BASE_URL = "https://beta.mrdekk.ru/todo"
    const val LIST = "$BASE_URL/list"

    fun getListOneUrl(id: String): String {
        return "$BASE_URL/list/$id"
    }
}