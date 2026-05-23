package com.swordfish.lemuroid.app.shared.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.swordfish.lemuroid.app.mobile.shared.compose.ui.BootingOverlay
import com.swordfish.lemuroid.app.shared.game.viewmodel.GameViewModelRetroGameView

@Composable
fun BaseGameScreen(
    viewModel: BaseGameScreenViewModel,
    gameScreen: @Composable (BaseGameScreenViewModel) -> Unit,
) {
    val gameState =
        viewModel.getGameState()
            .collectAsState(GameViewModelRetroGameView.GameState.Uninitialized)
            .value

    val isGameReady =
        gameState is GameViewModelRetroGameView.GameState.Loaded ||
            gameState is GameViewModelRetroGameView.GameState.Ready

    var animationFinished by remember { mutableStateOf(false) }

    if (isGameReady && animationFinished) {
        gameScreen(viewModel)
    } else {
        BootingOverlay(
            game = viewModel.game,
            isReady = isGameReady,
            onAnimationFinished = {
                animationFinished = true
            }
        )
    }
}
