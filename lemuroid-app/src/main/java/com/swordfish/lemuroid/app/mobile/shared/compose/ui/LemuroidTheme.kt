package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Nothing OS app theme — dynamic color (Material You) is intentionally disabled.
 * Supports both Light and Dark modes with a monochrome aesthetic.
 */

private val NothingDarkColorScheme =
    darkColorScheme(
        primary = AppPrimary,
        onPrimary = Color.White,
        primaryContainer = AppPrimary.copy(alpha = 0.7f),
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

private val NothingLightColorScheme =
    lightColorScheme(
        primary = AppPrimary,
        onPrimary = Color.Black,
        primaryContainer = AppPrimary.copy(alpha = 0.7f),
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

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useSurface: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) NothingDarkColorScheme else NothingLightColorScheme

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
