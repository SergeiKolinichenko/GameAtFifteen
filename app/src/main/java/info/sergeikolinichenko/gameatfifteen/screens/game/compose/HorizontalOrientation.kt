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

/** Created by Sergei Kolinichenko on 10.01.2023 at 18:01 (GMT+3) **/

@Composable
fun HorizontalOrientation(viewModel: GameViewModel) {

    val showWinDialog by
    viewModel.showWinDialog.observeAsState(WinDialogButtonState.WinDialogInitial)

    Row(        modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
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