package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme

/** Created by Sergei Kolinichenko on 07.01.2023 at 14:50 (GMT+3) **/

@Composable
fun GameBoard() {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        repeat(4) {
            RowButtons()
        }
    }
}

@Composable
private fun RowButtons() {
    Row(
//        modifier = Modifier
//            .fillMaxWidth()
    ) {
        repeat(4) {
            GameButton()
        }
    }
}

@Composable
private fun GameButton() {
    var x by remember {
        mutableStateOf("10")
    }
    Card(
        modifier = Modifier
            .requiredSize(100.dp)
            .padding(start = 2.dp, top = 2.dp)
            .clickable { x = onCardClick(x) },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            text = x,
            modifier = Modifier.wrapContentSize(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Black
        )
    }
}

private fun onCardClick(s: String): String {
    return if (s == "10") "15"
    else "10"
}

@Preview
@Composable
fun PreviewThemeLightGameBoard() {
    GameAtFifteenTheme(
        darkTheme = false
    ) {
        GameBoard()
    }
}

@Preview
@Composable
fun PreviewThemeDarkGameBoard() {
    GameAtFifteenTheme(
        darkTheme = true
    ) {
        GameBoard()
    }
}