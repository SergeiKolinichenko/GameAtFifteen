package info.sergeikolinichenko.gameatfifteen.screens.game.states

/** Created by Sergei Kolinichenko on 11.01.2023 at 18:40 (GMT+3) **/

sealed class GameControlButtonState{

    object InitialButtons : GameControlButtonState()

    object ButtonStatistics : GameControlButtonState()

    object ButtonStartGame : GameControlButtonState()

    object ButtonGameOver : GameControlButtonState()

}