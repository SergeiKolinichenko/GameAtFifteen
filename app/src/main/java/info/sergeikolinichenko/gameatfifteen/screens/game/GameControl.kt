package info.sergeikolinichenko.gameatfifteen.screens.game

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 08.01.2023 at 19:51 (GMT+3) **/

@Composable
fun GameControl() {

    val viewModel: GameViewModel = viewModel()
    val buttonState = viewModel.gameControlStates.observeAsState(
        initial = GameControlState.Initial
    )

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Box(modifier = Modifier.weight(1f)) {
                var text = "Time: 15:15"
                text = when(buttonState.value) {
                    GameControlState.ButtonStatistics -> "Statistic"
                    GameControlState.ButtonStartGame -> "Start"
                    GameControlState.ButtonGameOver -> "Over"
                    else -> "Time: 15:15"
                }

                GameTextInfo(text = text)
            }

            Box(modifier = Modifier.weight(1f)) {
                GameTextInfo(text = "Movies: 215")
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        GameButtons(
            titleButton = R.string.button_statistics,
            onClickListener = {
                viewModel.changeGameControlState(
                    GameControlState.ButtonStatistics
                )
            }
        )

        Spacer(modifier = Modifier.size(8.dp))

        Row {

            Box(modifier = Modifier.weight(1f)) {
                GameButtons(
                    titleButton = R.string.button_game_over,
                    onClickListener = {
                        viewModel.changeGameControlState(
                            GameControlState.ButtonGameOver
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                GameButtons(
                    titleButton = R.string.button_start_game,
                    onClickListener = {
                        viewModel.changeGameControlState(
                            GameControlState.ButtonStartGame
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Composable
private fun GameTextInfo(
    text: String
) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
        ) {

            ResponsiveTitleText(text = text)

        }
    }
}

@Composable
private fun GameButtons(
    titleButton: Int,
    onClickListener: () -> Unit
) {
    Button(
        onClick = { onClickListener() },
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.primaryVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = titleButton),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PreviewThemeLightGameControl() {
    GameAtFifteenTheme(
        darkTheme = false
    ) {
        GameControl()
    }
}

@Preview
@Composable
fun PreviewThemeDarkGameControl() {
    GameAtFifteenTheme(
        darkTheme = true
    ) {
        GameControl()
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
        textStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold)
    )
}