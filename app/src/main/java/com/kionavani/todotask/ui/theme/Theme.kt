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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LightColorScheme = lightColorScheme(    // TODO: Написать схему через датакласс
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

val DarkColorScheme = darkColorScheme(
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
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}