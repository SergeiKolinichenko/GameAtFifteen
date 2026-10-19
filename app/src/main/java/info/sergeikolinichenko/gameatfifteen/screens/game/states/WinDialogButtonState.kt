package info.sergeikolinichenko.gameatfifteen.screens.game.states

import info.sergeikolinichenko.gameatfifteen.R

/** Created by Sergei Kolinichenko on 24.01.2023 at 14:22 (GMT+3) **/

sealed class WinDialogButtonState(
    val titleButton: Int
) {

    object WinDialogButtonCancel: WinDialogButtonState(
        titleButton = R.string.win_dialog_title_button_cancel
    )

    object WinDialogButtonStart: WinDialogButtonState(
        titleButton = R.string.win_dialog_title_button_start
    )

}
