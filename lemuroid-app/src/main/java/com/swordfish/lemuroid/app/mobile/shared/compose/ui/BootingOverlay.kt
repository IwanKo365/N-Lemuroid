package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.swordfish.lemuroid.lib.library.db.entity.Game
import kotlin.math.roundToInt

@Composable
fun BootingOverlay(
    game: Game,
    onAnimationFinished: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeightPx = with(androidx.compose.ui.platform.LocalDensity.current) {
        configuration.screenHeightDp.dp.toPx()
    }
    
    val yOffset = remember { Animatable(screenHeightPx * 0.2f) } // Start a bit below middle

    LaunchedEffect(Unit) {
        // Slide up animation
        yOffset.animateTo(
            targetValue = -screenHeightPx,
            animationSpec = tween(durationMillis = 800)
        )
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(0, yOffset.value.roundToInt()) }
                .padding(16.dp)
                .size(width = 200.dp, height = 280.dp) // Adjusted size for the "cartridge" in animation
        ) {
            LemuroidGameCard(
                game = game,
                onClick = {},
                onLongClick = {}
            )
        }
    }
}
