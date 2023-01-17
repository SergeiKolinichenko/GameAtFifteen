package info.sergeikolinichenko.gameatfifteen.screens.game.states

/** Created by Sergei Kolinichenko on 14.01.2023 at 14:46 (GMT+3) **/

sealed class GameControlTextState {

    object Initial: GameControlTextState()

    class TextTimeElapsed(
        val timeElapsed: String
    ): GameControlTextState()

    class TextMovesNumber(
        val movesNumber: String
    ): GameControlTextState()

}