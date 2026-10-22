package info.sergeikolinichenko.gameatfifteen.ui.skins

import androidx.annotation.StringRes
import info.sergeikolinichenko.gameatfifteen.R

// Look of the board and its tiles. The order here is the order in the picker.
enum class TileSkin(@param:StringRes val title: Int) {
    CLASSIC(R.string.skin_classic),
    DARK_WOOD(R.string.skin_dark_wood),
    LIGHT_WOOD(R.string.skin_light_wood),
    ORANGE(R.string.skin_orange)
}
