package com.kionavani.todotask.data.remote

// TODO: ключ и ссылку в другое место
object Endpoints {
    private const val BASE_URL = "https://hive.mrdekk.ru/todo"
    const val LIST = "$BASE_URL/list"

    fun getListOneUrl(id: String): String {
        return "$BASE_URL/list/$id"
    }
}