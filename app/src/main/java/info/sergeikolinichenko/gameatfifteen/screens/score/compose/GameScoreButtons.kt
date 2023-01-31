package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel
import info.sergeikolinichenko.gameatfifteen.screens.score.state.GameScoreButtonState
import info.sergeikolinichenko.gameatfifteen.ui.theme.Shapes

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:57 (GMT+3) **/

@Composable
fun GameScoreButtons(
    viewModel: ScoreViewModel
) {

    val animationState by viewModel.animationState.observeAsState(false)
    val transition = updateTransition(targetState = animationState, label = "")

    val yOffset by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 250) },
        label = ""
    ) {
        if (it) 0f else 300f
    }

    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500) },
        label = ""
    ) {
        if (it) 1f else 0f
    }

    val xSize by transition.animateInt(
        transitionSpec = { tween(1500) },
        label = ""
    ) {
        if (it) 310 else 0
    }

    val ySize by transition.animateInt(
        transitionSpec = { tween(1500) },
        label = ""
    ) {
        if (it) 220 else 0
    }

    Box(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, bottom = 20.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                )
                .offset(y = yOffset.dp)
                .alpha(alpha)
                .height(ySize.dp)
                .width(xSize.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ModuleButton(
                titleButton = GameScoreButtonState.ButtonSortByDate.titleButton,
                onClick = {
                    viewModel.handlerButtons(GameScoreButtonState.ButtonSortByDate)
                }
            )

            Spacer(modifier = Modifier.size(4.dp))

            ModuleButton(
                titleButton = GameScoreButtonState.ButtonSortByMoves.titleButton,
                onClick = {
                    viewModel.handlerButtons(GameScoreButtonState.ButtonSortByMoves)
                }
            )

            Spacer(modifier = Modifier.size(4.dp))

            ModuleButton(
                titleButton = GameScoreButtonState.ButtonSortByTimer.titleButton,
                onClick = {
                    viewModel.handlerButtons(GameScoreButtonState.ButtonSortByTimer)
                }
            )

            Spacer(modifier = Modifier.size(4.dp))

            ModuleButton(
                titleButton = GameScoreButtonState.ButtonDeleteEverything.titleButton,
                onClick = {
                    viewModel.handlerButtons(GameScoreButtonState.ButtonDeleteEverything)
                }
            )

        }
    }


}

@Composable
private fun ModuleButton(
    titleButton: Int,
    onClick: (titleButton: Int) -> Unit
) {

    Button(
        onClick = { onClick(titleButton) },
        modifier = Modifier
            .width(300.dp)
            .height(50.dp),
        shape = Shapes.medium
    ) {

        Text(
            text = stringResource(titleButton),
            fontSize = 11.sp,
            fontWeight = FontWeight.W900
        )
    }
}
