package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.swordfish.lemuroid.lib.library.db.entity.Game
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun BootingOverlay(
    game: Game,
    isReady: Boolean,
    onAnimationFinished: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    val screenHeightPx = with(density) {
        configuration.screenHeightDp.dp.toPx()
    }
    
    // Navigation bar is 80dp + 24dp bottom padding = 104dp from bottom
    val navBarHeightPx = with(density) { 104.dp.toPx() }
    
    // The top of the dock should be at the "height of the navigation bar"
    val dockTargetY = (screenHeightPx / 2) - navBarHeightPx
    val stickingOutOffsetPx = with(density) { 60.dp.toPx() }
    
    // Height to reach the bottom of the screen from the target Y
    val dockHeightPx = navBarHeightPx + with(density) { 100.dp.toPx() } // Extra height to ensure it covers off-screen

    val cartridgeYOffset = remember { Animatable(dockTargetY - stickingOutOffsetPx) }
    val globalYOffset = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }

    var hasClickedIn by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "bobbing")
    val bobbingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbing"
    )

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(400))
    }

    LaunchedEffect(isReady) {
        if (isReady && !hasClickedIn) {
            // Haptic feedback for the "clunk"
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            
            cartridgeYOffset.animateTo(
                targetValue = dockTargetY,
                animationSpec = tween(durationMillis = 150)
            )
            hasClickedIn = true
            delay(500)
            
            globalYOffset.animateTo(
                targetValue = navBarHeightPx * 1.5f,
                animationSpec = tween(durationMillis = 400)
            )
            
            onAnimationFinished()
        }
    }

    // Indicator light color animation
    val indicatorColor by animateColorAsState(
        targetValue = if (isReady) Color.Red else Color.DarkGray,
        animationSpec = tween(300),
        label = "indicator"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = contentAlpha.value),
            contentAlignment = Alignment.Center
        ) {
            // The Cartridge
            Box(
                modifier = Modifier
                    .offset { 
                        val bob = if (!hasClickedIn) bobbingOffset else 0f
                        IntOffset(0, (cartridgeYOffset.value + globalYOffset.value + bob).roundToInt()) 
                    }
                    .padding(16.dp)
                    .size(width = 160.dp, height = 210.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LemuroidGameCard(
                    game = game,
                    onClick = {},
                    onLongClick = {}
                )
            }

            // The Console Dock / Slot
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset { IntOffset(0, (screenHeightPx / 2 + dockTargetY + globalYOffset.value).roundToInt()) }
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(with(density) { dockHeightPx.toDp() }),
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                tonalElevation = 6.dp,
                shadowElevation = 12.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                // UI Elements on the Console Dock
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp), // Shifting everything down
                    contentAlignment = Alignment.TopCenter
                ) {
                    // Wide "Game Boy" Screen, perfectly centered and cut off at the bottom
                    val screenBg = if (MaterialTheme.colorScheme.background == Color.Black) Color(0xFF0F0F0F) else Color(0xFFE8E8E8)
                    val screenBorder = if (MaterialTheme.colorScheme.background == Color.Black) Color.DarkGray else Color.LightGray
                    
                    Box(
                        modifier = Modifier
                            .width(260.dp) // Symmetrical and wide
                            .height(100.dp) // Less tall while still reaching the bottom
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(screenBg) // Theme-aware screen background
                            .border(2.dp, screenBorder, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    ) {
                        DotMatrixPlaceholder(
                            game = game,
                            backgroundColor = Color.Transparent,
                            showText = false, // No placeholder initials
                            modifier = Modifier.fillMaxSize() // Dots now fill the entire screen area
                        )
                    }

                    // Indicator Light (Positioned to the left of the centered screen)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(x = (-150).dp, y = 20.dp) // Balanced spacing to the left of the screen
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(indicatorColor)
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    )
                }
            }
        }
    }
}
