package com.swordfish.touchinput.radial.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Nothing OS GlassSurface — circular buttons with thin border, flat fill.
 * cornerRadius defaults to Dp.Infinity (fully round) to match the original
 * ControlFaceButtons layout geometry and eliminate overlap.
 *
 * Replaces:
 * lemuroid-touchinput/src/main/java/com/swordfish/touchinput/radial/ui/GlassSurface.kt
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = Dp.Infinity,
    fillColor: Color = Color.White.copy(alpha = 0.06f),
    shadowColor: Color = Color.Transparent,
    shadowWidth: Dp = 0.dp,
    content: @Composable BoxWithConstraintsScope.() -> Unit = {},
) {
    // Thin white border — the dot matrix panel feel
    val borderColor = Color.White.copy(alpha = 0.18f)
    val borderWidth = 1.dp

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier =
            modifier.drawWithCache {
                val cr =
                    if (cornerRadius == Dp.Infinity) {
                        size.minDimension / 2f
                    } else {
                        cornerRadius.toPx().coerceAtMost(size.minDimension / 2f)
                    }
                val bw = borderWidth.toPx()

                onDrawWithContent {
                    // Flat fill
                    drawRoundRect(
                        color = fillColor,
                        topLeft = Offset.Zero,
                        size = size,
                        cornerRadius = CornerRadius(cr, cr),
                    )
                    // Thin circular border
                    drawRoundRect(
                        color = borderColor,
                        topLeft = Offset(bw / 2f, bw / 2f),
                        size = Size(size.width - bw, size.height - bw),
                        cornerRadius = CornerRadius(cr, cr),
                        style = Stroke(width = bw),
                    )
                    drawContent()
                }
            },
        content = content,
    )
}
