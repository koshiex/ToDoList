package com.kionavani.todotask.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix

// Light theme colors
val LightSeparator = Color(0x33000000)
val LightOverlay = Color(0x0F000000)
val LightPrimaryLabel = Color(0xFF000000)
val LightSecondaryLabel = Color(0x99000000)
val LightTertiaryLabel = Color(0x4D000000)
val LightDisabledLabel = Color(0x26000000)
val LightRed = Color(0xFFFF3830)
val LightGreen = Color(0xFF34C759)
val LightBlue = Color(0xFF007AFF)
val TransparentLightBlue = Color(0x4D007AFF)
val LightGray = Color(0xFF8E8E93)
val LightGrayLight = Color(0xFFD1D1D6)
val LightWhite = Color(0xFFFFFFFF)
val LightBackPrimary = Color(0xFFF7F6F2)
val LightBackSecondary = Color(0xFFFFFFFF)
val LightBackElevated = Color(0xFFFFFFFF)

// Dark theme colors
val DarkSeparator = Color(0x33FFFFFF)
val DarkOverlay = Color(0x52000000)
val DarkPrimaryLabel = Color(0xFFFFFFFF)
val DarkSecondaryLabel = Color(0x99FFFFFF)
val DarkTertiaryLabel = Color(0x66FFFFFF)
val DarkDisabledLabel = Color(0x26FFFFFF)
val DarkRed = Color(0xFFFF453A)
val DarkGreen = Color(0xFF32D74B)
val DarkBlue = Color(0xFF0A84FF)
val TransparentDarkBlue = Color(0x4D007AFF)
val DarkGray = Color(0xFF8E8E93)
val DarkGrayLight = Color(0xFF48484A)
val DarkWhite = Color(0xFFFFFFFF)
val DarkBackPrimary = Color(0xFF161618)
val DarkBackSecondary = Color(0xFF252528)
val DarkBackElevated = Color(0xFF3C3C3F)


data class ColorScheme(
    val supportSeparator: Color,
    val supportOverlay: Color,
    val labelPrimary: Color,
    val labelSecondary: Color,
    val labelTertiary: Color,
    val labelDisable: Color,
    val colorRed: Color,
    val colorGreen: Color,
    val colorBlue: Color,
    val transparentBlue: Color,
    val colorGray: Color,
    val colorGrayLight: Color,
    val colorWhite: Color,
    val backPrimary: Color,
    val backSecondary: Color,
    val backElevated: Color
)

val LocalColorScheme = staticCompositionLocalOf {
    ColorScheme(
        supportSeparator = Color.Unspecified,
        supportOverlay = Color.Unspecified,
        labelPrimary = Color.Unspecified,
        labelSecondary = Color.Unspecified,
        labelTertiary = Color.Unspecified,
        labelDisable = Color.Unspecified,
        colorRed = Color.Unspecified,
        colorGreen = Color.Unspecified,
        colorBlue = Color.Unspecified,
        transparentBlue = Color.Unspecified,
        colorGray = Color.Unspecified,
        colorGrayLight = Color.Unspecified,
        colorWhite = Color.Unspecified,
        backPrimary = Color.Unspecified,
        backSecondary = Color.Unspecified,
        backElevated = Color.Unspecified
    )
}