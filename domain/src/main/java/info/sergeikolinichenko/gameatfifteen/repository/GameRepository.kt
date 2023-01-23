package info.sergeikolinichenko.gameatfifteen.repository

import info.sergeikolinichenko.gameatfifteen.models.StateGame

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:27 (GMT+3) **/

interface GameRepository {

    fun saveStateGame(stateGame: StateGame)

    fun restoreStateGame(): StateGame

}