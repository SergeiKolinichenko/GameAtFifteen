package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import android.content.res.Configuration.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

    when(orientation) {
        ORIENTATION_PORTRAIT -> OrientationPortrait(
            viewModel = viewModel,
            clickButtonStatistics = clickButtonStatistics
        )

        ORIENTATION_LANDSCAPE -> OrientationLandscape(
            viewModel = viewModel,
            clickButtonStatistics = clickButtonStatistics
        )

        ORIENTATION_UNDEFINED -> OrientationPortrait(
            viewModel = viewModel,
            clickButtonStatistics = clickButtonStatistics
        )
    }

    WinDialog(showWinDialog, viewModel)
}
