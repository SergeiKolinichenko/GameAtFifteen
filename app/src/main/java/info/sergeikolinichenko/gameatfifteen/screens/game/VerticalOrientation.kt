package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Created by Sergei Kolinichenko on 08.01.2023 at 21:05 (GMT+3) **/

@Composable
fun VerticalOrientation() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameControl()

        Spacer(modifier = Modifier.size(8.dp))

        GameBoard()
    }

}