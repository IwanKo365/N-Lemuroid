package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Nothing OS app theme — dynamic color (Material You) is intentionally disabled
 * so the Nothing red accent always shows, regardless of Samsung wallpaper color.
 *
 * This theme is forced to DARK mode to maintain the Nothing OS aesthetic.
 */

private val NothingDarkColorScheme =
    darkColorScheme(
        primary = AppPrimary,
        onPrimary = Color.White,
        primaryContainer = AppPrimary.copy(alpha = 0.7f),
        onPrimaryContainer = Color.White,
        secondary = AppDockBackground,
        onSecondary = Color.White,
        secondaryContainer = AppDockBackground.copy(alpha = 0.7f),
        onSecondaryContainer = Color.White,
        background = AppBackground,
        onBackground = AppOnBackground,
        surface = AppBackground,
        onSurface = AppOnBackground,
        surfaceVariant = AppCardBackground,
        onSurfaceVariant = AppOnBackground,
        outline = AppDockBackground,
        error = Color(0xFFFF3B30), // Also Nothing Red for errors
    )

@Composable
fun AppTheme(
    useSurface: Boolean = true,
    content: @Composable () -> Unit,
) {
    // We ignore the system theme and dynamic colors completely.
    // N-Lemuroid is always in its custom Dark mode.
    val colors = NothingDarkColorScheme

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
