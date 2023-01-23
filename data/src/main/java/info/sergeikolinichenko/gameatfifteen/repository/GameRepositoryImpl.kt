package info.sergeikolinichenko.gameatfifteen.repository

import android.content.SharedPreferences
import info.sergeikolinichenko.gameatfifteen.models.MapperStateGamePrefs
import info.sergeikolinichenko.gameatfifteen.models.StateGame
import info.sergeikolinichenko.gameatfifteen.models.StateGamePrefModel
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 18.01.2023 at 21:21 (GMT+3) **/

class GameRepositoryImpl @Inject constructor(
    private val mapperPrefs: MapperStateGamePrefs,
    private val sharedPreferences: SharedPreferences
) : GameRepository {

    override fun saveStateGame(stateGame: StateGame) {

        mapperPrefs.mapModelToPrefsModel(stateGame).apply {
            with(sharedPreferences.edit()) {
                putString(GAME_BUTTONS_STATE, state).apply()
                putInt(GAME_MOVES_NUMBER, moves).apply()
                putInt(GAME_TIME_ELAPSED, time).apply()
            }
        }
    }

    override fun restoreStateGame(): StateGame {

        with(sharedPreferences) {
            return mapperPrefs.mapPrefsModelToModel(
                StateGamePrefModel(
                    state = getString(GAME_BUTTONS_STATE, ERROR_STRING) ?: "",
                    moves = getInt(GAME_MOVES_NUMBER, ERROR_INT),
                    time = getInt(GAME_TIME_ELAPSED, ERROR_INT)
                )
            )
        }
    }

    companion object {
        private const val GAME_MOVES_NUMBER = "moves_number"
        private const val GAME_TIME_ELAPSED = "time_elapsed"
        private const val GAME_BUTTONS_STATE = "buttons_state"

        private const val ERROR_INT = -1
        private const val ERROR_STRING = "error_shar_pref"
    }

}