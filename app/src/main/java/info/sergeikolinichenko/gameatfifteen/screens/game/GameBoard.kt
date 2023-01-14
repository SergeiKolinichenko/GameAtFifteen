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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState.Companion.PLATE_LIST

/** Created by Sergei Kolinichenko on 07.01.2023 at 14:50 (GMT+3) **/

@Composable
fun GameBoard() {

    val viewModel: GameViewModel = viewModel()
//    val plateState by viewModel.gameBoard.observeAsState(
//        initial = PLATE_LIST
//    )

    var plateState by remember {
        mutableStateOf(PLATE_LIST)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        plateState.forEach { arrayOfGameBoardStates ->

            Row(modifier = Modifier.weight(1f)) {

                arrayOfGameBoardStates.forEach {  GameBoardState ->

                    Box(modifier = Modifier.weight(1f)) {

                        GameButton(state =  GameBoardState) {
                            plateState = onClickGameButton(it, plateState)
                            testArray(plateState)
                        }
                    }

                }

            }

        }

//        Box(modifier = Modifier.weight(1f)) {
//            RowButtons()
//        }
//        Box(modifier = Modifier.weight(1f)) {
//            RowButtons()
//        }
//        Box(modifier = Modifier.weight(1f)) {
//            RowButtons()
//        }
//        Box(modifier = Modifier.weight(1f)) {
//            RowButtons()
//        }
    }
}

//@Composable
//private fun RowButtons() {
//    Row {
//        Box(modifier = Modifier.weight(1f)) {
//            GameButton()
//        }
//        Box(modifier = Modifier.weight(1f)) {
//            GameButton()
//        }
//        Box(modifier = Modifier.weight(1f)) {
//            GameButton()
//        }
//        Box(modifier = Modifier.weight(1f)) {
//            GameButton()
//        }
//    }
//}

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

fun onClickGameButton(number: String, plateState: Array<Array<GameBoardState>>): Array<Array<GameBoardState>> {

    var indexX: Int = 0
    var indexY: Int = 0

    plateState.forEachIndexed { x, arrayOfGameBoardStates ->
        arrayOfGameBoardStates.forEachIndexed { y, gameBoardState ->
            if (gameBoardState.number == number) {
                indexX = x
                indexY = y
            }
        }
    }

    return makeMove(indexX, indexY, plateState)
}

private fun makeMove(x: Int, y: Int, plateState: Array<Array<GameBoardState>>): Array<Array<GameBoardState>> {

    val array = plateState

    testArray(array)

    val emptyX = getEmptyX(plateState)
    val emptyY = getEmptyY(plateState)

    array[emptyX][emptyY] = array[x][y]
    array[x][y] = GameBoardState.NoPlate

    testArray(array)

    return array
}

private fun getEmptyX(plateState: Array<Array<GameBoardState>>): Int {
    var indexX = 0
    plateState.forEachIndexed { x, arrayOfGameBoardStates ->
        arrayOfGameBoardStates.forEachIndexed { y, _ ->
            if (plateState[x][y] == GameBoardState.NoPlate) indexX = x
        }
    }
    return indexX
}

private fun getEmptyY(plateState: Array<Array<GameBoardState>>): Int {
    var indexY = 0
    plateState.forEachIndexed { x, arrayOfGameBoardStates ->
        arrayOfGameBoardStates.forEachIndexed { y, _ ->
            if (plateState[x][y] == GameBoardState.NoPlate) indexY = y
        }
    }
    return indexY
}

private fun testArray(array: Array<Array<GameBoardState>>) {
    array.forEach {
        it.forEach {
            Log.d("MyLog", "${it.number}, ")
        }
    }
}