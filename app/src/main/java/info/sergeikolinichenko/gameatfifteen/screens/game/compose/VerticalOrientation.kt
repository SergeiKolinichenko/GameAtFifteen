package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel

/** Created by Sergei Kolinichenko on 08.01.2023 at 21:05 (GMT+3) **/

@Composable
fun VerticalOrientation(viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            GameControl(viewModel = viewModel)
        }

        Spacer(modifier = Modifier.size(8.dp))

        Box(
            modifier = Modifier.weight(1f)
        ) {
            GameBoard(viewModel = viewModel)
        }

    }

        WinDialog(true)
}