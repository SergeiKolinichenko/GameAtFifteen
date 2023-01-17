package info.sergeikolinichenko.gameatfifteen.screens.game

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState.Companion.PLATE_ARRAY

/** Created by Sergei Kolinichenko on 07.01.2023 at 14:50 (GMT+3) **/

@Composable
fun GameBoard() {

    val viewModel: GameViewModel = viewModel()
    val plateState by viewModel.gameBoard.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

//        plateState.forEach { arrayOfGameBoardStates ->
//
//            Row(modifier = Modifier.weight(1f)) {
//
//                arrayOfGameBoardStates.forEach { GameBoardState ->
//
//                    Box(modifier = Modifier.weight(1f)) {
//
//                        GameButton(state = GameBoardState) {
//                            plateState = onClickGameButton(it, plateState)
//                            testArray(plateState)
//                        }
//                    }
//
//                }
//
//            }
//
//        }
        var counter = 0
        for (line in 1..4) {

            Row(modifier = Modifier.weight(1f)) {

                for (item in 1..4) {

                    Box(modifier = Modifier.weight(1f)) {

                        if (plateState != null) {
                            GameButton(state = plateState!![counter]) {
                                viewModel.onClickGameButton(it)
                            }
                        }
                        counter++

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
        Text(
            text = state.number,
            modifier = Modifier.wrapContentSize(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Black
        )
    }
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