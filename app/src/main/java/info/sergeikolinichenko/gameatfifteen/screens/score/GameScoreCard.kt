package info.sergeikolinichenko.gameatfifteen.screens.score

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 10.01.2023 at 19:47 (GMT+3) **/

@Composable
fun GameScoreCard() {

    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
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
                    text = "15.01.23 / 19:22",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

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
                    text = "0555 / 02.30.59",
                    modifier = Modifier.padding(horizontal = 4.dp)
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

@Preview
@Composable
fun PreviewThemeLightGameScoreCard() {
    GameAtFifteenTheme(
        darkTheme = false
    ) {
        GameScoreCard()
    }
}

@Preview
@Composable
fun PreviewThemeDarkGameScoreCard() {
    GameAtFifteenTheme(
        darkTheme = true
    ) {
        GameScoreCard()
    }
}