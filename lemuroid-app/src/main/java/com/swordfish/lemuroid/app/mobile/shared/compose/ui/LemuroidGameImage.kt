package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.swordfish.lemuroid.app.shared.covers.CoverUtils
import com.swordfish.lemuroid.lib.library.db.entity.Game

// Standard grayscale matrix — desaturates the image fully
private val grayscaleMatrix = ColorMatrix().apply { setToSaturation(0f) }

@Composable
fun LemuroidGameImage(
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
        loading = { DotMatrixPlaceholder(game) },
        error = { DotMatrixPlaceholder(game) },
    )
}

@Composable
private fun DotMatrixPlaceholder(game: Game) {
    val initials = remember(game) { computeInitials(game) }
    val dotColor = Color(0x1AFFFFFF)
    val dotBrush = remember {
        val bitmap = ImageBitmap(8, 8)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { color = dotColor }
        canvas.drawCircle(Offset(4f, 4f), 1f, paint)
        ShaderBrush(ImageShader(bitmap, TileMode.Repeated, TileMode.Repeated))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppCardBackground)
            .background(dotBrush),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            fontFamily = NdotFontFamily,
            color = Color.White,
            fontSize = 48.sp,
            letterSpacing = 2.sp
        )
    }
}

private fun computeInitials(game: Game): String {
    val sanitizedName = game.title.replace(Regex("\\(.*\\)"), "")
    return sanitizedName.asSequence()
        .filter { it.isDigit() or it.isUpperCase() or (it == '&') }
        .take(3)
        .joinToString("")
        .ifBlank { game.title.first().toString() }
        .uppercase()
}

@Preview
@Composable
fun DotMatrixPlaceholderPreview() {
    AppTheme {
        Box(modifier = Modifier.size(200.dp)) {
            DotMatrixPlaceholder(
                game = Game(
                    id = 1,
                    fileName = "Pokemon Crystal.gbc",
                    fileUri = "file://pokemon.gbc",
                    title = "Pokemon Crystal Version",
                    systemId = "gbc",
                    developer = "Game Freak",
                    coverFrontUrl = null,
                    lastIndexedAt = 0,
                    isFavorite = true
                )
            )
        }
    }
}
