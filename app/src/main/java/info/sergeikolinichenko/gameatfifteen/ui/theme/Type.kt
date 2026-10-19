package info.sergeikolinichenko.gameatfifteen.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

val Typography = Typography()

// Digits of equal width, so counters and table columns do not jump
val NumbersStyle = TextStyle(
    fontFeatureSettings = "tnum",
    fontWeight = FontWeight.SemiBold
)
