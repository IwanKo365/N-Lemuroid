package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.swordfish.lemuroid.lib.library.db.entity.Game

private val grayscaleMatrix = ColorMatrix().apply { setToSaturation(0f) }

@Composable
fun LemuroidSmallGameImage(
    modifier: Modifier = Modifier,
    game: Game,
) {
    SubcomposeAsyncImage(
        model =
            ImageRequest.Builder(LocalContext.current)
                .data(game.coverFrontUrl)
                .build(),
        contentDescription = game.title,
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1.0f),
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.colorMatrix(grayscaleMatrix),
        loading = { 
            DotMatrixPlaceholder(
                game = game, 
                fontSize = 14.sp,
                backgroundColor = Color.Transparent 
            ) 
        },
        error = { 
            DotMatrixPlaceholder(
                game = game, 
                fontSize = 14.sp,
                backgroundColor = Color.Transparent 
            ) 
        },
    )
}
