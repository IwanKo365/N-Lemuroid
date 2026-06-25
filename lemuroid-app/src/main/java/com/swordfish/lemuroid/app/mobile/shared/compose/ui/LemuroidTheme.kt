package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Nothing OS app theme — dynamic color (Material You) is intentionally disabled.
 * Supports both Light and Dark modes with a monochrome aesthetic.
 */

data class AppThemeSettings(
    val isGruvbox: Boolean = false
)

val LocalAppThemeSettings = staticCompositionLocalOf { AppThemeSettings() }

private fun getNothingDarkColorScheme(primaryColor: Color) =
    darkColorScheme(
        primary = primaryColor,
        onPrimary = Color.White,
        primaryContainer = primaryColor.copy(alpha = 0.7f),
        onPrimaryContainer = Color.White,
        secondary = AppDockBackground, // Dark grey for dock
        onSecondary = Color.White,
        secondaryContainer = AppDockBackground.copy(alpha = 0.7f),
        onSecondaryContainer = Color.White,
        background = Color.Black,
        onBackground = Color.White,
        surface = Color.Black,
        onSurface = Color.White,
        surfaceVariant = AppCardBackground,
        onSurfaceVariant = Color.White,
        outline = AppDockBackground,
        error = Color(0xFFFF3B30),
    )

private fun getNothingLightColorScheme(primaryColor: Color) =
    lightColorScheme(
        primary = primaryColor,
        onPrimary = Color.Black,
        primaryContainer = primaryColor.copy(alpha = 0.7f),
        onPrimaryContainer = Color.Black,
        secondary = AppDockBackgroundLight, // Light grey for dock
        onSecondary = Color.Black,
        secondaryContainer = AppDockBackgroundLight.copy(alpha = 0.7f),
        onSecondaryContainer = Color.Black,
        background = Color.White,
        onBackground = Color.Black,
        surface = Color.White,
        onSurface = Color.Black,
        surfaceVariant = Color.White,
        onSurfaceVariant = Color.Black,
        outline = AppDockBackgroundLight,
        error = Color(0xFFFF3B30),
    )

private fun getGruvboxDarkColorScheme() =
    darkColorScheme(
        primary = GruvboxRed,
        onPrimary = GruvboxFg0,
        primaryContainer = GruvboxRed.copy(alpha = 0.7f),
        onPrimaryContainer = GruvboxFg0,
        secondary = GruvboxBg1,
        onSecondary = GruvboxFg0,
        secondaryContainer = GruvboxBg1.copy(alpha = 0.7f),
        onSecondaryContainer = GruvboxFg0,
        background = GruvboxBg0,
        onBackground = GruvboxFg0,
        surface = GruvboxBg0,
        onSurface = GruvboxFg0,
        surfaceVariant = GruvboxBg1,
        onSurfaceVariant = GruvboxFg1,
        outline = GruvboxGray,
        error = GruvboxRed,
    )

private fun getGruvboxLightColorScheme() =
    lightColorScheme(
        primary = GruvboxRed,
        onPrimary = GruvboxBg0,
        primaryContainer = GruvboxRed.copy(alpha = 0.7f),
        onPrimaryContainer = GruvboxBg0,
        secondary = GruvboxBg1Light,
        onSecondary = GruvboxFg0Light,
        secondaryContainer = GruvboxBg1Light.copy(alpha = 0.7f),
        onSecondaryContainer = GruvboxFg0Light,
        background = GruvboxBg0Light,
        onBackground = GruvboxFg0Light,
        surface = GruvboxBg0Light,
        onSurface = GruvboxFg0Light,
        surfaceVariant = GruvboxBg1Light,
        onSurfaceVariant = GruvboxFg1Light,
        outline = GruvboxGray,
        error = GruvboxRed,
    )

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isGruvbox: Boolean = false,
    useSurface: Boolean = true,
    primaryColor: Color = AppPrimary,
    content: @Composable () -> Unit,
) {
    val colors = when {
        isGruvbox && darkTheme -> getGruvboxDarkColorScheme()
        isGruvbox && !darkTheme -> getGruvboxLightColorScheme()
        darkTheme -> getNothingDarkColorScheme(primaryColor)
        else -> getNothingLightColorScheme(primaryColor)
    }

    CompositionLocalProvider(LocalAppThemeSettings provides AppThemeSettings(isGruvbox = isGruvbox)) {
        MaterialTheme(colorScheme = colors) {
            if (useSurface) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    content()
                }
            } else {
                content()
            }
        }
    }
}
