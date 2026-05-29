package com.swordfish.lemuroid.common.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable

/**
 * Nothing OS dot matrix fallback cover.
 * Draws 3-letter game abbreviation as a dot matrix.
 */
class TextDrawable(
    private val text: String,
    private val bgColor: Int,
    private val isMonochrome: Boolean = true
) : Drawable() {

    private val dotColor = if (isMonochrome) Color.WHITE else Color.argb(40, 255, 255, 255)
    private val activeDotColor = Color.WHITE

    // 4-wide × 7-tall bitmap font
    private val font = mapOf(
        'A' to listOf("0110","1001","1001","1111","1001","1001","1001"),
        'B' to listOf("1110","1001","1001","1110","1001","1001","1110"),
        'C' to listOf("0111","1000","1000","1000","1000","1000","0111"),
        'D' to listOf("1110","1001","1001","1001","1001","1001","1110"),
        'E' to listOf("1111","1000","1000","1110","1000","1000","1111"),
        'F' to listOf("1111","1000","1000","1110","1000","1000","1000"),
        'G' to listOf("0111","1000","1000","1011","1001","1001","0111"),
        'H' to listOf("1001","1001","1001","1111","1001","1001","1001"),
        'I' to listOf("0110","0010","0010","0010","0010","0010","0110"),
        'J' to listOf("0011","0001","0001","0001","0001","1001","0110"),
        'K' to listOf("1001","1010","1100","1000","1100","1010","1001"),
        'L' to listOf("1000","1000","1000","1000","1000","1000","1111"),
        'M' to listOf("1001","1111","1101","1001","1001","1001","1001"),
        'N' to listOf("1001","1101","1101","1011","1011","1001","1001"),
        'O' to listOf("0110","1001","1001","1001","1001","1001","0110"),
        'P' to listOf("1110","1001","1001","1110","1000","1000","1000"),
        'Q' to listOf("0110","1001","1001","1001","1101","1011","0111"),
        'R' to listOf("1110","1001","1001","1110","1010","1001","1001"),
        'S' to listOf("0111","1000","1000","0110","0001","0001","1110"),
        'T' to listOf("1111","0100","0100","0100","0100","0100","0100"),
        'U' to listOf("1001","1001","1001","1001","1001","1001","0110"),
        'V' to listOf("1001","1001","1001","1001","1001","0110","0110"),
        'W' to listOf("1001","1001","1001","1001","1101","1111","1001"),
        'X' to listOf("1001","1001","0110","0110","0110","1001","1001"),
        'Y' to listOf("1001","1001","0110","0110","0100","0100","0100"),
        'Z' to listOf("1111","0001","0010","0110","1000","1000","1111"),
        '0' to listOf("0110","1001","1001","1001","1001","1001","0110"),
        '1' to listOf("0110","0010","0010","0010","0010","0010","0111"),
        '2' to listOf("0110","1001","0001","0010","0100","1000","1111"),
        '3' to listOf("1110","0001","0001","0110","0001","0001","1110"),
        '4' to listOf("1001","1001","1001","1111","0001","0001","0001"),
        '5' to listOf("1111","1000","1000","1110","0001","0001","1110"),
        '6' to listOf("0110","1000","1000","1110","1001","1001","0110"),
        '7' to listOf("1111","0001","0010","0010","0100","0100","0100"),
        '8' to listOf("0110","1001","1001","0110","1001","1001","0110"),
        '9' to listOf("0110","1001","1001","0111","0001","0001","0110"),
        '-' to listOf("0000","0000","0000","1111","0000","0000","0000"),
        '&' to listOf("0110","1001","1010","0100","1010","1001","0111"),
        '?' to listOf("0110","1001","0001","0010","0100","0000","0100"),
        ' ' to listOf("0000","0000","0000","0000","0000","0000","0000"),
    )

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun draw(canvas: Canvas) {
        val w = bounds.width().toFloat()
        val h = bounds.height().toFloat()

        // Background
        mPaint.color = bgColor
        canvas.drawRect(bounds, mPaint)

        // Dot grid background pattern
        val spacing = w / 32f
        val dotRadius = spacing / 3f
        mPaint.color = if (isMonochrome) Color.argb(50, 255, 255, 255) else Color.argb(40, 255, 255, 255)
        
        var x = spacing / 2f
        while (x < w) {
            var y = spacing / 2f
            while (y < h) {
                canvas.drawCircle(x, y, dotRadius, mPaint)
                y += spacing
            }
            x += spacing
        }

        val chars = text.uppercase().take(3)
        val n = chars.length
        if (n == 0) return

        val charCols   = 4
        val charRows   = 7
        val charGap    = 2
        val totalCols  = n * charCols + (n - 1) * charGap

        val cellW = w * 0.72f / totalCols
        val cellH = h * 0.72f / charRows
        val dotW  = cellW * 0.72f
        val dotH  = cellH * 0.72f
        val r     = minOf(dotW, dotH) * 0.28f

        val ox = (w - totalCols * cellW) / 2f
        val oy = (h - charRows * cellH) / 2f

        mPaint.color = activeDotColor

        chars.forEachIndexed { ci, ch ->
            val glyph = font[ch] ?: font['?']!!
            val colOffset = ci * (charCols + charGap)
            glyph.forEachIndexed { ri, row ->
                row.forEachIndexed { pi, bit ->
                    if (bit == '1') {
                        val cx = ox + (colOffset + pi) * cellW + cellW / 2f
                        val cy = oy + ri * cellH + cellH / 2f
                        val left   = cx - dotW / 2f
                        val top    = cy - dotH / 2f
                        val right  = cx + dotW / 2f
                        val bottom = cy + dotH / 2f
                        canvas.drawRoundRect(left, top, right, bottom, r, r, mPaint)
                    }
                }
            }
        }
    }

    override fun getOpacity(): Int = mPaint.alpha
    override fun getIntrinsicWidth(): Int = -1
    override fun getIntrinsicHeight(): Int = -1
    override fun setAlpha(alpha: Int) { mPaint.alpha = alpha; invalidateSelf() }
    override fun setColorFilter(p0: ColorFilter?) { mPaint.colorFilter = p0; invalidateSelf() }
}
