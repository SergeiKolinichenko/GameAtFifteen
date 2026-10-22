package info.sergeikolinichenko.gameatfifteen.ui.skins

import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.core.content.edit
import info.sergeikolinichenko.gameatfifteen.R
import javax.inject.Inject

// Look of the board and its tiles. The order here is the order in the picker.
enum class TileSkin(@param:StringRes val title: Int) {
    CLASSIC(R.string.skin_classic),
    DARK_WOOD(R.string.skin_dark_wood),
    LIGHT_WOOD(R.string.skin_light_wood),
    ORANGE(R.string.skin_orange)
}

class SkinStore @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun get(): TileSkin {
        val name = preferences.getString(KEY_TILE_SKIN, null)
        return TileSkin.entries.firstOrNull { it.name == name } ?: TileSkin.CLASSIC
    }

    fun set(skin: TileSkin) {
        preferences.edit { putString(KEY_TILE_SKIN, skin.name) }
    }

    private companion object {
        const val KEY_TILE_SKIN = "tile_skin"
    }
}
