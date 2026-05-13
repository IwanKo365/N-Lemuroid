package com.swordfish.touchinput.radial.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import gg.padkit.ui.DefaultCrossForeground

/**
 * Nothing OS dot matrix D-pad.
 * Arrows are drawn as pixel/dot grids instead of Material icons.
 *
 * Drop this into:
 * lemuroid-touchinput/src/main/java/com/swordfish/touchinput/radial/ui/LemuroidCrossForeground.kt
 */
@Composable
fun LemuroidCrossForeground(
    allowDiagonals: Boolean,
    directionState: State<Offset>,
) {
    DefaultCrossForeground(
        modifier = Modifier.fillMaxSize(),
        directionState = directionState,
        allowDiagonals = allowDiagonals,
        leftDial = { LemuroidDotArrowButton(it, DotArrowDirection.LEFT) },
        rightDial = { LemuroidDotArrowButton(it, DotArrowDirection.RIGHT) },
        topDial = { LemuroidDotArrowButton(it, DotArrowDirection.UP) },
        bottomDial = { LemuroidDotArrowButton(it, DotArrowDirection.DOWN) },
        foregroundComposite = { LemuroidCompositeForeground(it) },
    )
}

// ── Direction enum ────────────────────────────────────────────────────────────

enum class DotArrowDirection { UP, DOWN, LEFT, RIGHT }

// ── Dot matrix arrow bitmaps (5×5 grids, 1 = dot on) ────────────────────────
//
//  UP          DOWN        LEFT        RIGHT
//  . . X . .   . . x . .   . . X . .   . . X . .
//  . X X X .   . . X . .   . X X . .   . . X X .
//  X X X X X   X X X X X   X X X X X   X X X X X
//  . . X . .   . X X X .   . X X . .   . . X X .
//  . . x . .   . . X . .   . . X . .   . . X . .

private val DOT_UP =
    arrayOf(
        intArrayOf(0, 0, 1, 0, 0),
        intArrayOf(0, 1, 1, 1, 0),
        intArrayOf(1, 1, 1, 1, 1),
        intArrayOf(0, 0, 1, 0, 0),
        intArrayOf(0, 0, 1, 0, 0),
    )

private val DOT_DOWN =
    arrayOf(
        intArrayOf(0, 0, 1, 0, 0),
        intArrayOf(0, 0, 1, 0, 0),
        intArrayOf(1, 1, 1, 1, 1),
        intArrayOf(0, 1, 1, 1, 0),
        intArrayOf(0, 0, 1, 0, 0),
    )

private val DOT_LEFT =
    arrayOf(
        intArrayOf(0, 0, 1, 0, 0),
        intArrayOf(0, 1, 1, 0, 0),
        intArrayOf(1, 1, 1, 1, 1),
        intArrayOf(0, 1, 1, 0, 0),
        intArrayOf(0, 0, 1, 0, 0),
    )

private val DOT_RIGHT =
    arrayOf(
        intArrayOf(0, 0, 1, 0, 0),
        intArrayOf(0, 0, 1, 1, 0),
        intArrayOf(1, 1, 1, 1, 1),
        intArrayOf(0, 0, 1, 1, 0),
        intArrayOf(0, 0, 1, 0, 0),
    )

fun dotGridFor(direction: DotArrowDirection) =
    when (direction) {
        DotArrowDirection.UP -> DOT_UP
        DotArrowDirection.DOWN -> DOT_DOWN
        DotArrowDirection.LEFT -> DOT_LEFT
        DotArrowDirection.RIGHT -> DOT_RIGHT
    }

// ── Composable ────────────────────────────────────────────────────────────────

@Composable
fun LemuroidDotArrowButton(
    pressedState: State<Boolean>,
    direction: DotArrowDirection,
) {
    LemuroidButtonForeground(
        pressed = pressedState,
        label = { },
        icon = {
            NothingDotArrow(
                modifier =
                    Modifier
                        .size(maxWidth * 0.55f, maxHeight * 0.55f),
                direction = direction,
                pressed = pressedState,
            )
        },
    )
}
