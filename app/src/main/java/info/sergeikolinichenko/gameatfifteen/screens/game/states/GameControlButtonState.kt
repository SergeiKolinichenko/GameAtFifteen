package info.sergeikolinichenko.gameatfifteen.screens.game.states

/** Created by Sergei Kolinichenko on 11.01.2023 at 18:40 (GMT+3) **/

sealed class GameControlButtonState{

    object Initial : GameControlButtonState()

    object ButtonStatistics: GameControlButtonState()

    class ButtonStartGame(
        val enable: Boolean = true
    ): GameControlButtonState()

    object ButtonGameOver: GameControlButtonState()

}