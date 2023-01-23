package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlTextState.*
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 08.01.2023 at 19:51 (GMT+3) **/

@Composable
fun GameControl(viewModel: GameViewModel) {

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

            val textGame by viewModel.gameControlTextState.observeAsState(
                initial = InitialText
            )
            val textTimeElapsed = remember { mutableStateOf("") }
            val textMovesNumber = remember { mutableStateOf("") }

            when (textGame) {
                is TextTimeElapsed -> textTimeElapsed.value =
                    (textGame as TextTimeElapsed).timeElapsed

                is TextMovesNumber -> textMovesNumber.value =
                    (textGame as TextMovesNumber).movesNumber

                else -> {}
            }

            GameTexts(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                title = stringResource(id = R.string.text_time_elapsed),
                info = textTimeElapsed.value
            )

            Spacer(modifier = Modifier.size(4.dp))

            GameTexts(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                title = stringResource(id = R.string.text_move_number),
                info = textMovesNumber.value
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
                    viewModel.getPressedGameControlButton(
                        ButtonStatistics
                    )
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
                        viewModel.getPressedGameControlButton(
                            ButtonGameOver
                        )
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
        }
    }
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
            fontSize = 26.sp,
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
        textStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
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