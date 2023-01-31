package info.sergeikolinichenko.gameatfifteen.repository

import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.models.StateGame

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:27 (GMT+3) **/

interface GameRepository {

    fun setStateGame(stateGame: StateGame)

    fun getStateGame(): StateGame

    fun setListScoresSort(sort: String)

    fun getListScoresSort(): String
    suspend fun setScoreGame(scoreGame: ScoreGame)

    suspend fun deleteScoreGame(scoreGame: ScoreGame)

    suspend fun deleteAllScoresGame()

    fun <T> getListScoreGame(): T
}