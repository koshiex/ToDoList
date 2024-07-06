package com.kionavani.todotask.data

import androidx.annotation.StringRes
import com.kionavani.todotask.R

/**
 * Enum класс важности таски
 */
enum class Importance(@StringRes val displayName: Int) {
    LOW(R.string.importance_low),
    REGULAR(R.string.importance_regular),
    HIGH(R.string.importance_high);

    companion object {
        fun fromString(value: String): Importance {
            return when (value) {
                "low" -> LOW
                "basic" -> REGULAR
                "importance" -> HIGH
                else -> throw ClassCastException()
            }
        }
    }

    fun toStringValue(): String {
        return when (this) {
            LOW -> "low"
            REGULAR -> "basic"
            HIGH -> "importance"
        }
    }
}

fun String.toImportance(): Importance {
    return Importance.fromString(this)
}

fun Importance.toDtoName(): String {
    return this.toStringValue()
}
