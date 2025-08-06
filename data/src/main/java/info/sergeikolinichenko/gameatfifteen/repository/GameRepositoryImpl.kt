package info.sergeikolinichenko.gameatfifteen.repository

import android.content.SharedPreferences
import androidx.lifecycle.map
import info.sergeikolinichenko.gameatfifteen.database.GameDao
import info.sergeikolinichenko.gameatfifteen.models.MapperGameModels
import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.models.StateGame
import info.sergeikolinichenko.gameatfifteen.models.StateGamePrefModel
import javax.inject.Inject
import androidx.core.content.edit

/** Created by Sergei Kolinichenko on 18.01.2023 at 21:21 (GMT+3) **/

class GameRepositoryImpl @Inject constructor(
    private val mapper: MapperGameModels,
    private val sharedPreferences: SharedPreferences,
    private val dao: GameDao
) : GameRepository {

    override fun setStateGame(stateGame: StateGame) {

        mapper.mapEntityToPrefsModel(stateGame).apply {
            with(sharedPreferences.edit()) {
                putString(GAME_BUTTONS_STATE, state).apply()
                putInt(GAME_MOVES_NUMBER, moves).apply()
                putInt(GAME_TIME_ELAPSED, time).apply()
            }
        }
    }

    override fun getStateGame(): StateGame {

        with(sharedPreferences) {
            return mapper.mapPrefsModelToEntity(
                StateGamePrefModel(
                    state = getString(GAME_BUTTONS_STATE, ERROR_STRING) ?: "",
                    moves = getInt(GAME_MOVES_NUMBER, ERROR_INT),
                    time = getInt(GAME_TIME_ELAPSED, ERROR_INT)
                )
            )
        }
    }

    override fun setListScoresSort(sort: String) {
        sharedPreferences.edit { putString(GAME_LIST_SCORE_SORT, sort) }
    }

    override fun getListScoresSort(): String {
        return sharedPreferences.getString(GAME_LIST_SCORE_SORT, ERROR_STRING) ?: ""
    }

    override suspend fun setScoreGame(scoreGame: ScoreGame) {
        dao.addScore(mapper.mapEntityToDbModel(scoreGame))
    }

    override suspend fun deleteScoreGame(scoreGame: ScoreGame) {
        dao.deleteScore(mapper.mapEntityToDbModel(scoreGame))
    }

    override suspend fun deleteAllScoresGame() {
        dao.deleteAllScores()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getListScoreGame(): T {
        return dao.getListScores().map {
            mapper.mapListDbModelToListEntity(it)
        } as T
    }

    companion object {
        private const val GAME_MOVES_NUMBER = "moves_number"
        private const val GAME_TIME_ELAPSED = "time_elapsed"
        private const val GAME_BUTTONS_STATE = "buttons_state"
        private const val GAME_LIST_SCORE_SORT = "list_score_sort"

        private const val ERROR_INT = -1
        private const val ERROR_STRING = ""
    }

}