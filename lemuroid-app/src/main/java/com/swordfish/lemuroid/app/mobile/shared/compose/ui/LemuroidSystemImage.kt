package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.swordfish.lemuroid.app.shared.systems.MetaSystemInfo

/**
 * Nothing OS — system card image.
 * - Dot matrix background (consistent with game placeholders)
 * - Icon tinted pure white
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
    val dotColor = Color(0x1AFFFFFF)
    val dotBrush = remember {
        val bitmap = ImageBitmap(8, 8)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { color = dotColor }
        canvas.drawCircle(Offset(4f, 4f), 1.5f, paint)
        ShaderBrush(ImageShader(bitmap, TileMode.Repeated, TileMode.Repeated))
    }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(2.0f) 
                .background(AppCardBackground)
                .background(dotBrush),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxSize(0.58f),
            painter = painterResource(id = system.metaSystem.imageResId),
            contentDescription = stringResource(id = system.metaSystem.titleResId),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.colorMatrix(whitenMatrix),
        )
    }
}
