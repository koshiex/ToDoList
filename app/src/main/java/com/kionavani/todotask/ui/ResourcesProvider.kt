package com.kionavani.todotask.ui

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.StringRes

/**
 * Позволяет удобно получить доступ к системным ресурсам
 */
class ResourcesProvider(
    private val appContext: Context
) {
    private var currentContext: Context = appContext

    fun attachActivityContext(context: Context) {
        currentContext = context
    }

    fun detachActivityContext() {
        currentContext = appContext
    }

    fun getString(@StringRes resID: Int): String = currentContext.getString(resID)
}