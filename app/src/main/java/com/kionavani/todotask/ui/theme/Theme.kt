package com.kionavani.todotask.ui.theme

import android.app.Activity
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

val LightColorScheme = lightColorScheme(
    primary = LightBackPrimary,
    secondary = LightBackSecondary,
    background = LightBackPrimary,
    surface = LightBackElevated,
    onPrimary = LightPrimaryLabel,
    onSecondary = LightSecondaryLabel,
    onBackground = LightOverlay,
    onSurface = LightSeparator,
    primaryContainer = LightBackSecondary,
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryLabel,
    secondary = DarkSecondaryLabel,
    background = DarkBackPrimary,
    surface = DarkBackElevated,
    onPrimary = DarkWhite,
    onSecondary = DarkWhite,
    onBackground = DarkPrimaryLabel,
    onSurface = DarkPrimaryLabel,
    primaryContainer = DarkBackElevated,
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