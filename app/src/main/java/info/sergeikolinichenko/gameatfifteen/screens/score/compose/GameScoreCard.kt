package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 10.01.2023 at 19:47 (GMT+3) **/

@Composable
fun GameScoreCard() {

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .fillMaxWidth()
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp, horizontal = 4.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primaryVariant
                )
            ) {

                ResponsiveGameScoreCardText(
                    text = "15.01.23 | 19:22 | 0555 | 0.20.59",
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .weight(1f)
                )
            }
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
        textStyle = TextStyle(
            fontSize = 22.sp
        )
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