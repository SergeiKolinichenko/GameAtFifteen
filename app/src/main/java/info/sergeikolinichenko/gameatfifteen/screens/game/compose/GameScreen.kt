package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.WinDialogButtonState

/** Created by Sergei Kolinichenko on 24.01.2023 at 20:19 (GMT+3) **/

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    clickButtonStatistics: () -> Unit
) {

    val showWinDialog by
    viewModel.showWinDialog.observeAsState(WinDialogButtonState.WinDialogInitial)

    val orientation by viewModel.orientationScreen.observeAsState()

    if (orientation == ORIENTATION_PORTRAIT) {
        VerticalOrientation(
            viewModel = viewModel,
            clickButtonStatistics = clickButtonStatistics
        )
    } else {
        HorizontalOrientation(
            viewModel = viewModel,
            clickButtonStatistics = clickButtonStatistics
        )
    }

    WinDialog(showWinDialog, viewModel)
}

@Composable
private fun HorizontalOrientation(
    viewModel: GameViewModel,
    clickButtonStatistics: () -> Unit
) {


    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            GameControl(
                viewModel = viewModel,
                clickButtonStatistics = clickButtonStatistics
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Box(
            modifier = Modifier.weight(1f)
        ) {
            GameBoard(viewModel = viewModel)
        }
    }
}

@Composable
private fun VerticalOrientation(
    viewModel: GameViewModel,
    clickButtonStatistics: () -> Unit
) {

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
            GameControl(
                viewModel = viewModel,
                clickButtonStatistics = clickButtonStatistics
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Box(
            modifier = Modifier.weight(1f)
        ) {
            GameBoard(viewModel = viewModel)
        }

    }
}