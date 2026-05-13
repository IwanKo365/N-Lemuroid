package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.swordfish.lemuroid.app.shared.covers.CoverUtils
import com.swordfish.lemuroid.lib.library.db.entity.Game

/**
 * Nothing OS — game card image.
 * Box art is displayed in grayscale (monochrome) using a Coil ColorFilter.
 * Fallback (no cover) uses the dot matrix TextDrawable on Nothing red.
 *
 * Replaces:
 * lemuroid-app/src/main/java/com/swordfish/lemuroid/app/mobile/shared/compose/ui/LemuroidGameImage.kt
 */

// Standard grayscale matrix — desaturates the image fully
private val grayscaleMatrix = ColorMatrix().apply { setToSaturation(0f) }

@Composable
fun LemuroidGameImage(
    modifier: Modifier = Modifier,
    game: Game,
) {
    val fallbackDrawable = remember(game) { CoverUtils.getFallbackDrawable(game) }
    val fallbackPainter  = rememberDrawablePainter(drawable = fallbackDrawable)

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(game.coverFrontUrl)
            .build(),
        contentDescription = game.title,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.0f),
        fallback      = fallbackPainter,
        error         = fallbackPainter,
        contentScale  = ContentScale.Crop,
        colorFilter   = ColorFilter.colorMatrix(grayscaleMatrix),  // monochrome box art
    )
}
