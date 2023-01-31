package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.ResponsiveText

/** Created by Sergei Kolinichenko on 07.01.2023 at 14:50 (GMT+3) **/

@Composable
fun GameBoard (viewModel: GameViewModel) {

    val plateState by viewModel.gameBoard.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        plateState?.forEach { arrayOfGameBoardStates ->

            Row(modifier = Modifier.weight(1f)) {

                arrayOfGameBoardStates.forEach { GameBoardState ->

                    Box(modifier = Modifier.weight(1f)) {

                        GameButton(state = GameBoardState) {
                            viewModel.onClickGameButton(it)
                        }
                    }

                }

            }

        }
    }
}

@Composable
private fun GameButton(
    state: GameBoardState,
    onClickListener: (number: String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 2.dp, top = 2.dp)
            .clickable { onClickListener(state.number) },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            color = MaterialTheme.colors.onPrimary
        )
    ) {

        ResponsiveTitleText(modifier = Modifier.wrapContentSize(), text = state.number)
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
        textStyle = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Black),
        textAlign = TextAlign.End
    )
}

//@Preview
//@Composable
//fun PreviewThemeLightGameBoard() {
//    GameAtFifteenTheme(
//        darkTheme = false
//    ) {
//        GameBoard()
//    }
//}
//
//@Preview
//@Composable
//fun PreviewThemeDarkGameBoard() {
//    GameAtFifteenTheme(
//        darkTheme = true
//    ) {
//        GameBoard()
//    }
//}