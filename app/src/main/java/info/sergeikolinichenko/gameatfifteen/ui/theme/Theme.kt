package info.sergeikolinichenko.gameatfifteen.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryNight,
    inversePrimary = PrimaryDay,
    onPrimary = OnPrimaryNight,
    primaryContainer = PrimaryContainerNight,
    onPrimaryContainer = OnPrimaryContainerNight,
    secondary = SecondaryNight,
    onSecondary = OnSecondaryNight,
    secondaryContainer = SecondaryContainerNight,
    onSecondaryContainer = OnSecondaryContainerNight,
    background = BackgroundNight,
    onBackground = OnBackgroundNight,
    surface = BackgroundNight,
    onSurface = OnBackgroundNight,
    surfaceVariant = SurfaceVariantNight,
    onSurfaceVariant = OnSurfaceVariantNight,
    surfaceContainer = SurfaceContainerNight,
    surfaceContainerHigh = SurfaceVariantNight,
    outline = OutlineNight
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryDay,
    inversePrimary = PrimaryNight,
    onPrimary = OnPrimaryDay,
    primaryContainer = PrimaryContainerDay,
    onPrimaryContainer = OnPrimaryContainerDay,
    secondary = SecondaryDay,
    onSecondary = OnSecondaryDay,
    secondaryContainer = SecondaryContainerDay,
    onSecondaryContainer = OnSecondaryContainerDay,
    background = BackgroundDay,
    onBackground = OnBackgroundDay,
    surface = BackgroundDay,
    onSurface = OnBackgroundDay,
    surfaceVariant = SurfaceVariantDay,
    onSurfaceVariant = OnSurfaceVariantDay,
    surfaceContainer = SurfaceContainerDay,
    surfaceContainerHigh = SurfaceVariantDay,
    outline = OutlineDay
)

@Composable
fun GameAtFifteenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
