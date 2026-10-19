package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel
import info.sergeikolinichenko.gameatfifteen.screens.score.state.GameScoreButtonState
import info.sergeikolinichenko.gameatfifteen.screens.score.state.GameScoreButtonState.Companion.listGameScoreButtonState
import info.sergeikolinichenko.gameatfifteen.ui.theme.Shapes

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:57 (GMT+3) **/

private const val MAIN_HEIGHT = 280
private const val HALF_HEIGHT = 140
private const val ZERO = 0

@Composable
fun GameScoreButtons(
    viewModel: ScoreViewModel,
    offsetY: Float
) {

    var showDeleteAllDialog by rememberSaveable { mutableStateOf(false) }

    val transition = updateTransition(targetState = offsetY, label = "")

    val yOffset by transition.animateInt(
        transitionSpec = { tween(durationMillis = 500) },
        label = ""
    ) {

        if (it == ZERO.toFloat()) MAIN_HEIGHT
        else if (it > - HALF_HEIGHT) MAIN_HEIGHT + it.toInt()
        else ZERO
    }

    Box(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .offset(y = yOffset.dp)
            .padding(10.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            for (item in listGameScoreButtonState) {

                ModuleButton(stateButton = item, onClick = {
                    if (it is GameScoreButtonState.ButtonDeleteEverything) {
                        showDeleteAllDialog = true
                    } else {
                        viewModel.handlerButtons(state = it)
                    }
                })

            }
        }
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = { Text(text = stringResource(R.string.delete_all_dialog_title)) },
            text = { Text(text = stringResource(R.string.delete_all_dialog_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteAllDialog = false
                    viewModel.handlerButtons(state = GameScoreButtonState.ButtonDeleteEverything)
                }) {
                    Text(text = stringResource(R.string.delete_all_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteAllDialog = false
                    viewModel.hideButtons()
                }) {
                    Text(text = stringResource(R.string.delete_all_dialog_dismiss))
                }
            }
        )
    }
}

@Composable
private fun ModuleButton(
    stateButton: GameScoreButtonState,
    onClick: (stateButton: GameScoreButtonState) -> Unit
) {

    Button(
        onClick = { onClick(stateButton) },
        modifier = Modifier
            .padding(4.dp)
            .width(300.dp)
            .height(60.dp),
        shape = Shapes.medium
    ) {

        Text(
            text = stringResource(stateButton.titleButton),
            fontSize = 11.sp,
            fontWeight = FontWeight.W900
        )
    }
}
