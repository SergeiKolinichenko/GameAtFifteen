package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import kotlinx.coroutines.delay

/** Created by Sergei Kolinichenko on 17.02.2023 at 19:57 (GMT+3) **/

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OrientationLandscape(
    viewModel: GameViewModel,
    clickButtonStatistics: () -> Unit
) {

    var show by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        delay(50)
        show = true
    }

    AnimatedVisibility(
        visible = show,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                GameControl(
                    viewModel = viewModel,
                    clickButtonStatistics = clickButtonStatistics
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Box(
                modifier = Modifier.weight(1f)
            ) {
                GameBoard(viewModel = viewModel)
            }
        }
    }
}