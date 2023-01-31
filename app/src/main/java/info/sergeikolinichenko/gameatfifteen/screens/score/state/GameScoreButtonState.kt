package info.sergeikolinichenko.gameatfifteen.screens.score.state

import info.sergeikolinichenko.gameatfifteen.R

/** Created by Sergei Kolinichenko on 28.01.2023 at 20:26 (GMT+3) **/

sealed class GameScoreButtonState(
    val titleButton: Int
) {

    object ButtonSortByMoves: GameScoreButtonState(
        titleButton = R.string.button_sort_by_moves
    )

    object ButtonSortByTimer: GameScoreButtonState(
        titleButton = R.string.button_sort_by_time
    )

    object ButtonSortByDate: GameScoreButtonState(
        titleButton = R.string.button_sort_by_date
    )

    object ButtonDeleteEverything: GameScoreButtonState(
        titleButton = R.string.button_delete_all_scores_item
    )
}
