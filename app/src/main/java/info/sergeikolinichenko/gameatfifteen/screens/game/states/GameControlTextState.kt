package info.sergeikolinichenko.gameatfifteen.screens.game.states

/** Created by Sergei Kolinichenko on 14.01.2023 at 14:46 (GMT+3) **/

sealed class GameControlTextState(
    val info: String
) {

    object Initial: GameControlTextState(
        info = TEXT_INITIAL
    )

    object TextTimeElapsed: GameControlTextState(
        info = TEXT_TIME_ELAPSED
    )

    object TextMoveNumber: GameControlTextState(
        info = TEXT_MOVE_NUMBER
    )

    private companion object{
        private const val TEXT_INITIAL = "-----"
        private const val TEXT_TIME_ELAPSED = "00:00"
        private const val TEXT_MOVE_NUMBER = "0000"
    }
}