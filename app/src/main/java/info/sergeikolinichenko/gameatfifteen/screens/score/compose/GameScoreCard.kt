package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.screens.game.ResponsiveText

/** Created by Sergei Kolinichenko on 10.01.2023 at 19:47 (GMT+3) **/

@Composable
fun GameScoreCard(
    modifier: Modifier = Modifier,
    score: String
) {

    Card(
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primaryVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {

        Box(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            ResponsiveGameScoreCardText(
                text = score,
            )
        }
    }
}

@Composable
private fun ResponsiveGameScoreCardText(
    modifier: Modifier = Modifier,
    text: String,
) {
    ResponsiveText(
        modifier = modifier,
        text = text,
        textStyle = TextStyle( fontSize = 25.sp , fontWeight = FontWeight.W500),
        textAlign = TextAlign.Center
    )
}

//@Preview
//@Composable
//fun PreviewThemeLightGameScoreCard() {
//    GameAtFifteenTheme(
//        darkTheme = false
//    ) {
//        GameScoreCard()
//    }
//}
//
//@Preview
//@Composable
//fun PreviewThemeDarkGameScoreCard() {
//    GameAtFifteenTheme(
//        darkTheme = true
//    ) {
//        GameScoreCard()
//    }
//}