package com.kionavani.todotask.ui.theme

import android.app.Activity
import android.hardware.lights.Light
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LightMaterialScheme = lightColorScheme(
    primary = LightBackPrimary,
    secondary = LightBackSecondary,
    tertiary = LightBackElevated,
    onPrimary = LightPrimaryLabel,
    onSecondaryContainer = LightSecondaryLabel,
    onTertiary = LightTertiaryLabel,
    outline = LightSeparator,
    surface = LightOverlay,
    onSurface = LightBackElevated,
    inverseSurface = TransparentLightBlue,
    inverseOnSurface = LightBlue,
    inversePrimary = LightGreen,
    error = LightRed
)

val DarkMaterialScheme = darkColorScheme(
    primary = DarkBackPrimary,
    secondary = DarkBackSecondary,
    tertiary = DarkBackElevated,
    onPrimary = DarkPrimaryLabel,
    onSecondaryContainer = DarkSecondaryLabel,
    onTertiary = DarkTertiaryLabel,
    outline = DarkSeparator,
    surface = DarkOverlay,
    onSurface = DarkBackElevated,
    inverseSurface = TransparentDarkBlue,
    inverseOnSurface = DarkBlue,
    inversePrimary = DarkGreen,
    error = DarkRed
)

@Composable
fun ToDoTaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.backPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    CompositionLocalProvider(LocalColorScheme provides colorScheme) {
        MaterialTheme(
            typography = Typography,
            content = content,
            colorScheme = if (darkTheme) DarkMaterialScheme else LightMaterialScheme
        )
    }
}

object ToDoTaskTheme {
    val colorScheme: ColorScheme
        @Composable get() = LocalColorScheme.current
}

private val darkColorScheme = ColorScheme(
    supportSeparator = DarkSeparator,
    supportOverlay = DarkOverlay,
    labelPrimary = DarkPrimaryLabel,
    labelSecondary = DarkSecondaryLabel,
    labelTertiary = DarkTertiaryLabel,
    labelDisable = DarkDisabledLabel,
    colorRed = DarkRed,
    colorGreen = DarkGreen,
    colorBlue = DarkBlue,
    transparentBlue = TransparentDarkBlue,
    colorGray = DarkGray,
    colorGrayLight = DarkGrayLight,
    colorWhite = DarkWhite,
    backPrimary = DarkBackPrimary,
    backSecondary = DarkBackSecondary,
    backElevated = DarkBackElevated
)

private val lightColorScheme = ColorScheme(
    supportSeparator = LightSeparator,
    supportOverlay = LightOverlay,
    labelPrimary = LightPrimaryLabel,
    labelSecondary = LightSecondaryLabel,
    labelTertiary = LightTertiaryLabel,
    labelDisable = LightDisabledLabel,
    colorRed = LightRed,
    colorGreen = LightGreen,
    colorBlue = LightBlue,
    transparentBlue = TransparentLightBlue,
    colorGray = LightGray,
    colorGrayLight = LightGrayLight,
    colorWhite = LightWhite,
    backPrimary = LightBackPrimary,
    backSecondary = LightBackSecondary,
    backElevated = LightBackElevated
)