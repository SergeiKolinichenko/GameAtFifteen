package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme

/** Created by Sergei Kolinichenko on 08.01.2023 at 19:51 (GMT+3) **/

@Composable
fun GameControl() {
    Column(
//        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
            ) {

                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Time:",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "15:15",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
            ) {

                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Movies:",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "215",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Start Game",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "Game Over",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Statistics",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Preview
@Composable
fun PreviewThemeLightGameControl() {
    GameAtFifteenTheme(
        darkTheme = false
    ) {
        GameControl()
    }
}

@Preview
@Composable
fun PreviewThemeDarkGameControl() {
    GameAtFifteenTheme(
        darkTheme = true
    ) {
        GameControl()
    }
}