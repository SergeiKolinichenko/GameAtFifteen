package info.sergeikolinichenko.gameatfifteen.screens.score

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 10.01.2023 at 18:55 (GMT+3) **/

@Composable
fun GameScore() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        // Title
        Card(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background
        ) {

            Row {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ResponsiveGameScoreText(text = "Date / Time")
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    ResponsiveGameScoreText(text = "Movies / Times")
                }
            }

        }

        // List of score card
        Column(
            modifier = Modifier.weight(1f)
        ) {
            GameScoreCard()
            GameScoreCard()
            GameScoreCard()
            GameScoreCard()
            GameScoreCard()
            GameScoreCard()
            GameScoreCard()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Game\nOver",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Sort by\nMovies",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.size(4.dp))

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Sort by\nTimes",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ResponsiveGameScoreText(
    modifier: Modifier = Modifier,
    text: String,
) {
    ResponsiveText(
        modifier = modifier,
        text = text,
        textStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    )
}

@Preview
@Composable
fun PreviewThemeLightGameScore() {
    GameAtFifteenTheme(
        darkTheme = false
    ) {
        GameScore()
    }
}

@Preview
@Composable
fun PreviewThemeDarkGameScore() {
    GameAtFifteenTheme(
        darkTheme = true
    ) {
        GameScore()
    }
}