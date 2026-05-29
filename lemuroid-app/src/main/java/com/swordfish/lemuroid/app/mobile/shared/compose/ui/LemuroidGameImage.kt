package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.swordfish.lemuroid.R
import com.swordfish.lemuroid.app.shared.covers.CoverUtils
import com.swordfish.lemuroid.app.utils.android.settings.booleanPreferenceState
import com.swordfish.lemuroid.lib.library.db.entity.Game
import kotlin.math.abs

// Standard grayscale matrix — desaturates the image fully
private val grayscaleMatrix = ColorMatrix().apply { setToSaturation(0f) }

private val PlaceholderColors = listOf(
    Color(0xFFFF3B30), // Nothing Red
    Color(0xFFFFC700), // Nothing Yellow
    Color(0xFF007AFF), // Blue
    Color(0xFF34C759), // Green
    Color(0xFF5856D6), // Indigo
    Color(0xFFAF52DE), // Purple
    Color(0xFFFF9500)  // Orange
)

@Composable
fun LemuroidGameImage(
    modifier: Modifier = Modifier,
    game: Game,
) {
    val monochromeIcons = booleanPreferenceState(R.string.pref_key_monochrome_icons, true).value

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
        colorFilter = if (monochromeIcons) ColorFilter.colorMatrix(grayscaleMatrix) else null,
        loading = { DotMatrixPlaceholder(game, fontSize = 48.sp) },
        error = { DotMatrixPlaceholder(game, fontSize = 48.sp) },
    )
}

@Composable
fun DotMatrixPlaceholder(
    game: Game,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 48.sp,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    showText: Boolean = true
) {
    val isMonochrome = booleanPreferenceState(R.string.pref_key_monochrome_icons, true).value
    
    val bgColor = if (isMonochrome) {
        backgroundColor
    } else {
        val colorIndex = abs(game.id) % PlaceholderColors.size
        PlaceholderColors[colorIndex]
    }

    val initials = remember(game) { computeInitials(game) }
    val dotColor = if (isMonochrome) {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
    } else {
        Color.White.copy(alpha = 0.15f)
    }
    
    val dotBrush = remember(dotColor) {
        val bitmap = ImageBitmap(8, 8)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { color = dotColor }
        canvas.drawCircle(Offset(4f, 4f), 2.0f, paint)
        ShaderBrush(ImageShader(bitmap, TileMode.Repeated, TileMode.Repeated))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .background(dotBrush),
        contentAlignment = Alignment.Center
    ) {
        if (showText) {
            Text(
                text = initials,
                fontFamily = NdotFontFamily,
                color = if (isMonochrome) MaterialTheme.colorScheme.onBackground else Color.White,
                fontSize = fontSize,
                letterSpacing = 2.sp
            )
        }
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
