package info.sergeikolinichenko.gameatfifteen.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileSkin
import javax.inject.Inject

// The player's choices that outlive a game
class GameSettings @Inject constructor(
    private val preferences: SharedPreferences
) {

    var skin: TileSkin
        get() {
            val name = preferences.getString(KEY_TILE_SKIN, null)
            return TileSkin.entries.firstOrNull { it.name == name } ?: TileSkin.CLASSIC
        }
        set(value) = preferences.edit { putString(KEY_TILE_SKIN, value.name) }

    var boardSize: Int
        get() = preferences.getInt(KEY_BOARD_SIZE, GameBoardState.DEFAULT_SIZE)
            .takeIf { it in GameBoardState.SIZES } ?: GameBoardState.DEFAULT_SIZE
        set(value) = preferences.edit { putInt(KEY_BOARD_SIZE, value) }

    private companion object {
        const val KEY_TILE_SKIN = "tile_skin"
        const val KEY_BOARD_SIZE = "board_size"
    }
}
