package com.kionavani.todotask.data

import androidx.annotation.StringRes
import com.kionavani.todotask.R

/**
 * Enum класс важности таски
 */
enum class Importance(@StringRes val displayName: Int, @StringRes val description: Int) {
    LOW(R.string.importance_low, R.string.low_importance_descr),
    REGULAR(R.string.importance_regular, R.string.regular_importance_descr),
    HIGH(R.string.importance_high, R.string.high_importance_descr);

    companion object {
        fun fromString(value: String): Importance {
            return when (value) {
                "low" -> LOW
                "basic" -> REGULAR
                "important" -> HIGH
                else -> throw ClassCastException()
            }
        }
    }

    fun toStringValue(): String {
        return when (this) {
            LOW -> "low"
            REGULAR -> "basic"
            HIGH -> "important"
        }
    }
}

fun String.toImportance(): Importance {
    return Importance.fromString(this)
}

fun Importance.toDtoName(): String {
    return this.toStringValue()
}
