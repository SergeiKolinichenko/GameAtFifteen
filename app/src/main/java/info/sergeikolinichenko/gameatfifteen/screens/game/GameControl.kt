package info.sergeikolinichenko.gameatfifteen.screens.game

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlButtonState
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 08.01.2023 at 19:51 (GMT+3) **/

@Composable
fun GameControl() {

    val viewModel: GameViewModel = viewModel()

    Log.d("MyLog", "GameControl")

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
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val textTimeElapsed by viewModel.textTimeElapsed.observeAsState(initial = "00:00")

                    ResponsiveTitleText(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        text = stringResource(id = R.string.text_time_elapsed)
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
                            text = textTimeElapsed,
                        )
                    }
                }

            Spacer(modifier = Modifier.size(4.dp))

                val textMoveNumber by viewModel.textMoveNumber.observeAsState(initial = "0000")

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ResponsiveTitleText(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = stringResource(id = R.string.text_move_number)
                    )

                    Card(
                        modifier = Modifier
                            .width(130.dp)
                            .align(Alignment.CenterVertically),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colors.primaryVariant
                        )
                    ) {

                        ResponsiveTitleText(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                                .align(Alignment.CenterVertically),
                            text = textMoveNumber
                        )
                    }
                }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
        ) {

            GameButtons(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                titleButton = R.string.button_statistics,
                onClickListener = {
                    viewModel.changeGameControlButtonState(
                        GameControlButtonState.ButtonStatistics
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
                    onClickListener = {
                        viewModel.changeGameControlButtonState(
                            GameControlButtonState.ButtonGameOver
                        )
                    }
                )


                Spacer(modifier = Modifier.size(8.dp))

                GameButtons(
                    modifier = Modifier.weight(1f),
                    titleButton = R.string.button_start_game,
                    onClickListener = {
                        viewModel.changeGameControlButtonState(
                            GameControlButtonState.ButtonStartGame
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun GameButtons(
    modifier: Modifier = Modifier,
    titleButton: Int,
    onClickListener: () -> Unit
) {
    Button(
        onClick = { onClickListener() },
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.primaryVariant
        ),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = titleButton),
            fontSize = 26.sp,
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
        textStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
        textAlign = TextAlign.End
    )
}