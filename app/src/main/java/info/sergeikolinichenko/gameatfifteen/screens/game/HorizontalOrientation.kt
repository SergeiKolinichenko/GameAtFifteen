package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Created by Sergei Kolinichenko on 10.01.2023 at 18:01 (GMT+3) **/

@Composable
fun HorizontalOrientation() {

    Row(        modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            GameControl()
        }

        Spacer(modifier = Modifier.size(8.dp))

        Box(
            modifier = Modifier.weight(1f)
        ) {
            GameBoard()
        }
    }

}