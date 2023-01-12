package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.GameBoardState.Companion.PLATE_LIST

/** Created by Sergei Kolinichenko on 07.01.2023 at 14:50 (GMT+3) **/

@Composable
fun GameBoard() {

    val viewModel: GameViewModel = viewModel()
    val plateState by viewModel.gameBoard.observeAsState(initial = PLATE_LIST)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        for (line in plateState.indices) {

            Row(modifier = Modifier.weight(1f)) {

                for (item in plateState[line].indices) {

                    val title = plateState[line][item].number

                    Box(modifier = Modifier.weight(1f)) {
                        GameButton(title = title) {

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
    title: String,
    onClickListener: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 2.dp, top = 2.dp)
            .clickable { onClickListener() },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            color = MaterialTheme.colors.onPrimary
        )
    ) {
        Text(
            text = title,
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