package com.kionavani.todotask.ui

import androidx.annotation.StringRes
import com.kionavani.todotask.R

enum class ThemeState(
    @StringRes val displayName: Int
) {
    DARK(R.string.dark_theme), LIGHT(R.string.light_theme), SYSTEM(R.string.system_theme)
}