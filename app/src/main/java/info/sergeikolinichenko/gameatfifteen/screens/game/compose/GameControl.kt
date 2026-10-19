package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.TextButton
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlButtonState.*
import info.sergeikolinichenko.gameatfifteen.screens.game.ResponsiveText

/** Created by Sergei Kolinichenko on 08.01.2023 at 19:51 (GMT+3) **/

@Composable
fun GameControl(
    viewModel: GameViewModel,
    clickButtonStatistics: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            val textTimeElapsed by viewModel.timeElapsedText.observeAsState("")
            val textMovesNumber by viewModel.movesNumberText.observeAsState("")

            GameTexts(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                title = stringResource(id = R.string.text_time_elapsed),
                info = textTimeElapsed
            )

            Spacer(modifier = Modifier.size(4.dp))

            GameTexts(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                title = stringResource(id = R.string.text_move_number),
                info = textMovesNumber
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
        ) {

            val buttonGame by viewModel.gameControlButtonState.observeAsState(
                initial = InitialButtons
            )

            var showGiveUpDialog by rememberSaveable { mutableStateOf(false) }

            val stateButtonStatistics: Boolean
            val stateButtonGameOver: Boolean
            val stateButtonStartGame: Boolean

            when (buttonGame) {
                is ButtonStartGame -> {
                    stateButtonStatistics = false
                    stateButtonGameOver = true
                    stateButtonStartGame = false
                }
                is ButtonGameOver -> {
                    stateButtonStatistics = true
                    stateButtonGameOver = false
                    stateButtonStartGame = true
                }
                else -> {
                    stateButtonStatistics = true
                    stateButtonGameOver = false
                    stateButtonStartGame = true
                }
            }

            GameButtons(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                titleButton = R.string.button_statistics,
                enableButton = stateButtonStatistics,
                onClickListener = {
                    clickButtonStatistics()
                }
            )

            Spacer(modifier = Modifier.size(10.dp))

            Row(
                modifier = Modifier.weight(1.5f)
            ) {

                GameButtons(
                    modifier = Modifier.weight(1f),
                    titleButton = R.string.button_game_over,
                    enableButton = stateButtonGameOver,
                    onClickListener = {
                        showGiveUpDialog = true
                    }
                )

                Spacer(modifier = Modifier.size(8.dp))

                GameButtons(
                    modifier = Modifier.weight(1f),
                    titleButton = R.string.button_start_game,
                    enableButton = stateButtonStartGame,
                    onClickListener = {
                        viewModel.getPressedGameControlButton(
                            ButtonStartGame
                        )
                    }
                )
            }

            if (showGiveUpDialog) {
                GiveUpDialog(
                    onConfirm = {
                        showGiveUpDialog = false
                        viewModel.getPressedGameControlButton(ButtonGameOver)
                    },
                    onDismiss = { showGiveUpDialog = false }
                )
            }
        }
    }
}

@Composable
private fun GiveUpDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.give_up_dialog_title)) },
        text = { Text(text = stringResource(R.string.give_up_dialog_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.give_up_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.give_up_dialog_dismiss))
            }
        }
    )
}

@Composable
private fun GameTexts(
    modifier: Modifier = Modifier,
    title: String,
    info: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ResponsiveTitleText(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = title
        )

        Card(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colors.primaryVariant
            )
        ) {

            ResponsiveTitleText(
                modifier = Modifier
                    .width(130.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .align(Alignment.CenterVertically),
                text = info,
            )
        }
    }
}

@Composable
private fun GameButtons(
    modifier: Modifier = Modifier,
    titleButton: Int,
    enableButton: Boolean = true,
    onClickListener: () -> Unit
) {
    Button(
        onClick = { onClickListener() },
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.primaryVariant
        ),
        modifier = modifier,
        enabled = enableButton
    ) {
        Text(
            text = stringResource(id = titleButton),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ResponsiveTitleText(
    modifier: Modifier = Modifier,
    text: String,
) {
    ResponsiveText(
        modifier = modifier,
        text = text,
        textStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
        textAlign = TextAlign.End
    )
}

//@Preview
//@Composable
//fun PreviewThemeLightGameControl() {
//    GameAtFifteenTheme(
//        darkTheme = false
//    ) {
//        GameControl()
//    }
//}
//
//@Preview
//@Composable
//fun PreviewThemeDarkGameControl() {
//    GameAtFifteenTheme(
//        darkTheme = true
//    ) {
//        GameControl()
//    }
//}