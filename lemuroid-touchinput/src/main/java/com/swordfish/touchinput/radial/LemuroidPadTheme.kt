package com.swordfish.touchinput.radial

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Nothing OS dot matrix inspired pad theme.
 * Pure monochrome, sharp edges, no glass effect.
 *
 * Drop this into:
 * lemuroid-touchinput/src/main/java/com/swordfish/touchinput/radial/LemuroidPadTheme.kt
 */
class LemuroidPadTheme {

    // --- Nothing OS palette ---
    // Unpressed: dim white outlines, very low opacity fill
    // Pressed:   bright white fill, sharp invert

    val foregroundPadding: Dp = 6.dp
    val padding: Dp = 3.dp

    // Button face (the round/square button surface)
    private val buttonFill         = Color(1f, 1f, 1f, 0.06f)   // barely visible
    private val buttonFillPressed  = Color(1f, 1f, 1f, 0.85f)   // bright white on press

    // Composite (D-pad quadrant highlight)
    private val compositeFill         = Color(1f, 1f, 1f, 0.04f)
    private val compositeFillPressed  = Color(1f, 1f, 1f, 0.70f)

    // Icons / labels on buttons
    private val iconColor         = Color(1f, 1f, 1f, 0.55f)    // dim dot matrix white
    private val iconColorPressed  = Color(0f, 0f, 0f, 0.90f)    // black on bright press

    // Background plate behind each control group
    val level1Fill        = Color(1f, 1f, 1f, 0.04f)
    val level1Shadow      = Color.Transparent               // Nothing = no soft shadows
    val level1ShadowWidth = 0.dp

    // Outer container
    val level0Fill        = Color(1f, 1f, 1f, 0.02f)
    val level0Shadow      = Color.Transparent
    val level0ShadowWidth = 0.dp
    val level0CornerRadius = 0.dp                           // sharp corners

    // Level 2 (composite / quadrant)
    val level2Shadow      = Color.Transparent
    val level2ShadowWidth = 0.dp

    // Level 3 (button foreground)
    val level3Shadow      = Color.Transparent
    val level3ShadowWidth = 0.dp

    fun compositeFill(pressed: Boolean): Color =
        if (pressed) compositeFillPressed else compositeFill

    fun foregroundFill(pressed: Boolean): Color =
        if (pressed) buttonFillPressed else buttonFill

    fun icons(pressed: Boolean): Color =
        if (pressed) iconColorPressed else iconColor
}

val LocalLemuroidPadTheme =
    compositionLocalOf<LemuroidPadTheme> {
        error("LemuroidPadTheme is missing")
    }
