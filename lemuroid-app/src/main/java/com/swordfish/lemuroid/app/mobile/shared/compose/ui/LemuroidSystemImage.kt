package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.swordfish.lemuroid.app.mobile.shared.compose.ui.AppCardBackground
import com.swordfish.lemuroid.app.shared.systems.MetaSystemInfo

/**
 * Nothing OS — system card image.
 * - Wider card (2:1 aspect ratio) so icon has more breathing room horizontally
 * - Icon scaled down vertically so letters aren't too tall
 * - Background: Nothing red (#FF3B30)
 * - Icon tinted pure white (grayscale + whiten ColorFilter)
 */

// ColorMatrix that turns any color → white (for the dot matrix icons)
private val whitenMatrix =
    ColorMatrix(
        floatArrayOf(
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f,
        ),
    )

@Composable
fun LemuroidSystemImage(system: MetaSystemInfo) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(2.0f) // wider card: 2:1 ratio gives icons more horizontal space
                .background(AppCardBackground),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxSize(0.58f),
            // smaller fill so tall letters don't dominate
            painter = painterResource(id = system.metaSystem.imageResId),
            contentDescription = stringResource(id = system.metaSystem.titleResId),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.colorMatrix(whitenMatrix),
        )
    }
}
