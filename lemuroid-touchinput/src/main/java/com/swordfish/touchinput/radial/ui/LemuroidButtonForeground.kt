package com.swordfish.touchinput.radial.ui

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.swordfish.touchinput.radial.LocalLemuroidPadTheme

/**
 * Nothing OS version of LemuroidButtonForeground.
 * Labels (A, B, X, Y...) are rendered as 5×5 dot matrix glyphs via NothingDotLabel.
 * Icons (Start, Select, Menu) keep using vector drawables.
 *
 * Replaces:
 * lemuroid-touchinput/src/main/java/com/swordfish/touchinput/radial/ui/LemuroidButtonForeground.kt
 */
@Composable
fun LemuroidButtonForeground(
    modifier: Modifier = Modifier,
    pressed: State<Boolean>,
    label: (@Composable BoxWithConstraintsScope.() -> Unit),
    icon: (@Composable BoxWithConstraintsScope.() -> Unit),
) {
    val theme = LocalLemuroidPadTheme.current

    GlassSurface(
        modifier = modifier.fillMaxSize().padding(theme.foregroundPadding),
        fillColor = theme.foregroundFill(pressed.value),
        shadowColor = theme.level3Shadow,
        shadowWidth = theme.level3ShadowWidth,
        content = {
            icon()
            label()
        },
    )
}

@Composable
fun LemuroidButtonForeground(
    modifier: Modifier = Modifier,
    pressed: State<Boolean>,
    label: String? = null,
    icon: Int? = null,
    iconScale: Float = 0.5f,
    labelScale: Float = 1.0f,
) {
    LemuroidButtonForeground(
        modifier = modifier,
        pressed = pressed,
        // Dot matrix label instead of Text
        label = {
            if (label != null) {
                NothingDotLabel(
                    modifier = Modifier.size(maxWidth * 0.62f * labelScale, maxHeight * 0.62f * labelScale),
                    text = label,
                    pressed = pressed,
                )
            }
        },
        icon = { LemuroidButtonForegroundIcon(icon, iconScale, pressed) },
    )
}

@Composable
private fun BoxWithConstraintsScope.LemuroidButtonForegroundIcon(
    icon: Int?,
    scale: Float,
    pressedState: State<Boolean>,
) {
    if (icon == null) return
    Icon(
        modifier = Modifier.size(maxWidth * scale, maxHeight * scale),
        painter = painterResource(icon),
        contentDescription = "",
        tint = LocalLemuroidPadTheme.current.icons(pressedState.value),
    )
}
