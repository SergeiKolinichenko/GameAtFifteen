package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.WinDialogButtonState

/** Created by Sergei Kolinichenko on 08.01.2023 at 21:05 (GMT+3) **/

@Composable
fun VerticalOrientation(viewModel: GameViewModel) {

    val showWinDialog by
    viewModel.showWinDialog.observeAsState(WinDialogButtonState.WinDialogInitial)

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

    WinDialog(showWinDialog, viewModel)
}