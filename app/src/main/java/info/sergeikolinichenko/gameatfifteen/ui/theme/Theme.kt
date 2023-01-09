package info.sergeikolinichenko.gameatfifteen.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = PrimaryColorNight,
    primaryVariant = PrimaryLightColorNight,
    secondary = SecondaryColorNight,

    background = SecondaryDarkColorNight,
    surface = PrimaryDarkColorNight,
    onPrimary = TextColorNight,
    onSecondary = TextColorNight,
    onBackground = TextColorNight,
    onSurface = TextColorNight,

)

private val LightColorPalette = lightColors(
    primary = PrimaryColorDay,
    primaryVariant = PrimaryDarkColorDay,
    secondary = SecondaryColorDay,


    background = SecondaryLightColorDay,
    surface = PrimaryLightColorDay,
    onPrimary = TextColorDay,
    onSecondary = TextColorDay,
    onBackground = TextColorDay,
    onSurface = TextColorDay,

)

@Composable
fun GameAtFifteenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}