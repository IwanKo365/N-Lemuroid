package com.swordfish.touchinput.radial.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.swordfish.touchinput.radial.LocalLemuroidPadTheme

/**
 * Nothing OS dot matrix arrow icon drawn on Canvas.
 * Each "dot" is a small rounded square, arranged in a 5×5 grid.
 *
 * This is a NEW file — place it at:
 * lemuroid-touchinput/src/main/java/com/swordfish/touchinput/radial/ui/NothingDotArrow.kt
 */
@Composable
fun NothingDotArrow(
    modifier: Modifier = Modifier,
    direction: DotArrowDirection,
    pressed: State<Boolean>,
) {
    val theme = LocalLemuroidPadTheme.current
    val grid  = dotGridFor(direction)
    val color = theme.icons(pressed.value)

    Canvas(modifier = modifier) {
        val cols = 5
        val rows = 5

        // Leave a small margin so dots don't touch the edge
        val margin = size.minDimension * 0.04f
        val availW = size.width  - margin * 2f
        val availH = size.height - margin * 2f

        val cellW = availW / cols
        val cellH = availH / rows

        // Dot is 70% of the cell, centred
        val dotW = cellW * 0.70f
        val dotH = cellH * 0.70f
        val cornerR = minOf(dotW, dotH) * 0.30f   // slightly rounded square

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (grid[row][col] == 0) continue

                val cx = margin + col * cellW + cellW / 2f
                val cy = margin + row * cellH + cellH / 2f

                drawRoundRect(
                    color = color,
                    topLeft = Offset(cx - dotW / 2f, cy - dotH / 2f),
                    size = Size(dotW, dotH),
                    cornerRadius = CornerRadius(cornerR, cornerR),
                )
            }
        }
    }
}
