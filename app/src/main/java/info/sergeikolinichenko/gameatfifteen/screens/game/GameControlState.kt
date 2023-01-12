package info.sergeikolinichenko.gameatfifteen.screens.game

import info.sergeikolinichenko.gameatfifteen.R

/** Created by Sergei Kolinichenko on 11.01.2023 at 18:40 (GMT+3) **/

sealed class GameControlState {

    object Initial : GameControlState()

    object ButtonStatistics: GameControlState()

    object ButtonStartGame: GameControlState()

    object ButtonGameOver: GameControlState()

    class TextGameElapsed(
        val title: Int = R.string.text_time_elapsed
    ): GameControlState()

    class TextNumberMove(
        val title: Int = R.string.text_move_number
    ): GameControlState()
}