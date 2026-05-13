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
 * Nothing OS dot matrix font for button labels (A, B, X, Y, L, R, etc.)
 * Each letter is a 5×5 dot grid drawn on Canvas.
 *
 * NEW FILE — place at:
 * lemuroid-touchinput/src/main/java/com/swordfish/touchinput/radial/ui/NothingDotLabel.kt
 */

// ── 5×5 dot matrix glyphs ─────────────────────────────────────────────────────
// 1 = dot on, 0 = dot off, read top-to-bottom, left-to-right

private val GLYPH_A = arrayOf(
    intArrayOf(0,1,1,1,0),
    intArrayOf(1,0,0,0,1),
    intArrayOf(1,1,1,1,1),
    intArrayOf(1,0,0,0,1),
    intArrayOf(1,0,0,0,1),
)
private val GLYPH_N = arrayOf(
    intArrayOf(1,0,0,0,1),
    intArrayOf(1,1,0,0,1),
    intArrayOf(1,0,1,0,1),
    intArrayOf(1,0,0,1,1),
    intArrayOf(1,0,0,0,1),
)
private val GLYPH_B = arrayOf(
    intArrayOf(1,1,1,1,0),
    intArrayOf(1,0,0,0,1),
    intArrayOf(1,1,1,1,0),
    intArrayOf(1,0,0,0,1),
    intArrayOf(1,1,1,1,0),
)
private val GLYPH_X = arrayOf(
    intArrayOf(1,0,0,0,1),
    intArrayOf(0,1,0,1,0),
    intArrayOf(0,0,1,0,0),
    intArrayOf(0,1,0,1,0),
    intArrayOf(1,0,0,0,1),
)
private val GLYPH_Y = arrayOf(
    intArrayOf(1,0,0,0,1),
    intArrayOf(0,1,0,1,0),
    intArrayOf(0,0,1,0,0),
    intArrayOf(0,0,1,0,0),
    intArrayOf(0,0,1,0,0),
)
private val GLYPH_C = arrayOf(
    intArrayOf(0,1,1,1,0),
    intArrayOf(1,0,0,0,1),
    intArrayOf(1,0,0,0,0),
    intArrayOf(1,0,0,0,1),
    intArrayOf(0,1,1,1,0),
)
private val GLYPH_Z = arrayOf(
    intArrayOf(1,1,1,1,1),
    intArrayOf(0,0,0,1,0),
    intArrayOf(0,0,1,0,0),
    intArrayOf(0,1,0,0,0),
    intArrayOf(1,1,1,1,1),
)
private val GLYPH_L = arrayOf(
    intArrayOf(0,1,0,0,0),
    intArrayOf(0,1,0,0,0),
    intArrayOf(0,1,0,0,0),
    intArrayOf(0,1,0,0,0),
    intArrayOf(0,1,1,1,0),
)
private val GLYPH_R = arrayOf(
    intArrayOf(0,1,1,1,0),
    intArrayOf(0,1,0,0,1),
    intArrayOf(0,1,1,1,0),
    intArrayOf(0,1,1,0,0),
    intArrayOf(0,1,0,1,0),
)
private val GLYPH_1 = arrayOf(
    intArrayOf(0,0,1,0,0),
    intArrayOf(0,1,1,0,0),
    intArrayOf(0,0,1,0,0),
    intArrayOf(0,0,1,0,0),
    intArrayOf(0,1,1,1,0),
)
private val GLYPH_2 = arrayOf(
    intArrayOf(0,1,1,1,0),
    intArrayOf(1,0,0,0,1),
    intArrayOf(0,0,1,1,0),
    intArrayOf(0,1,0,0,0),
    intArrayOf(1,1,1,1,1),
)
private val GLYPH_UNKNOWN = arrayOf(
    intArrayOf(0,1,1,1,0),
    intArrayOf(1,0,0,0,1),
    intArrayOf(0,0,1,1,0),
    intArrayOf(0,0,0,0,0),
    intArrayOf(0,0,1,0,0),
)

private fun glyphFor(char: Char): Array<IntArray> = when (char.uppercaseChar()) {
    'A'  -> GLYPH_A
    'N'  -> GLYPH_N
    'B'  -> GLYPH_B
    'X'  -> GLYPH_X
    'Y'  -> GLYPH_Y
    'C'  -> GLYPH_C
    'Z'  -> GLYPH_Z
    'L'  -> GLYPH_L
    'R'  -> GLYPH_R
    '1'  -> GLYPH_1
    '2'  -> GLYPH_2
    else -> GLYPH_UNKNOWN
}

// ── Composable ────────────────────────────────────────────────────────────────

/**
 * Renders a single character (or the first char of [text]) as a 5×5 dot matrix.
 * For multi-char labels like "L1", "R2" — renders only the first char.
 * The full label is still readable because the button context makes it clear.
 */
@Composable
fun NothingDotLabel(
    modifier: Modifier = Modifier,
    text: String,
    pressed: State<Boolean>,
) {
    val theme = LocalLemuroidPadTheme.current
    val color = theme.icons(pressed.value)
    val char  = text.firstOrNull() ?: return
    val glyph = glyphFor(char)

    Canvas(modifier = modifier) {
        val cols = 5
        val rows = 5
        val margin = size.minDimension * 0.06f
        val availW = size.width  - margin * 2f
        val availH = size.height - margin * 2f
        val cellW  = availW / cols
        val cellH  = availH / rows
        val dotW   = cellW * 0.72f
        val dotH   = cellH * 0.72f
        val cornerR = minOf(dotW, dotH) * 0.28f

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (glyph[row][col] == 0) continue
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
