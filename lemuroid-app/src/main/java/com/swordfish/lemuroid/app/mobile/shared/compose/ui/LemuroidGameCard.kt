package com.swordfish.lemuroid.app.mobile.shared.compose.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swordfish.lemuroid.lib.library.GameSystem
import com.swordfish.lemuroid.lib.library.db.entity.Game

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun LemuroidGameCard(
    modifier: Modifier = Modifier,
    game: Game,
    onClick: () -> Unit = { },
    onLongClick: () -> Unit = { },
) {
    val context = LocalContext.current
    val systemName = context.getString(GameSystem.findById(game.systemId).shortTitleResId)

    Column(
        modifier = modifier
            .border(1.dp, AppCardBorder)
            .background(AppCardBackground)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .background(AppCardPillBackground, RoundedCornerShape(100))
                .border(1.dp, AppCardBorder, RoundedCornerShape(100))
                .padding(horizontal = 12.dp, vertical = 2.dp)
        ) {
            Text(
                text = systemName,
                fontFamily = NdotFontFamily,
                style = MaterialTheme.typography.labelSmall,
                color = AppOnBackground,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        HorizontalDivider(color = AppCardBorder, thickness = 1.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.border(1.dp, AppCardBorder)
            ) {
                LemuroidGameImage(game = game)
            }
        }
        Text(
            text = game.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 4.dp, end = 4.dp),
            fontFamily = NdotFontFamily,
            style = MaterialTheme.typography.labelSmall,
            color = AppOnBackground,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun LemuroidGameCardPreview() {
    AppTheme {
        LemuroidGameCard(
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
