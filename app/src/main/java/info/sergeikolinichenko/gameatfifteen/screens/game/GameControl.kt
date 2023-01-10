package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 08.01.2023 at 19:51 (GMT+3) **/

@Composable
fun GameControl() {

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Card(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                ) {

                    ResponsiveExampleText(text = "Time: 15:15")

//                    Text(
//                        text = "Time:",
//                        fontSize = FONT_SIZE_TEXT.sp,
//                        fontWeight = FontWeight.ExtraBold
//                    )
//                    Spacer(modifier = Modifier.size(8.dp))
//                    Text(
//                        text = "15:15",
//                        fontSize = FONT_SIZE_TEXT.sp,
//                        fontWeight = FontWeight.Bold
//                    )

                }
            }

            Card(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                ) {

                    ResponsiveExampleText(text = "Movies: 215")

//                    Text(
//                        text = "Movies:",
//                        fontSize = FONT_SIZE_TEXT.sp,
//                        fontWeight = FontWeight.ExtraBold
//                    )
//                    Spacer(modifier = Modifier.size(8.dp))
//                    Text(
//                        text = "215",
//                        fontSize = FONT_SIZE_TEXT.sp,
//                        fontWeight = FontWeight.Bold
//                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = { /*TODO*/ },
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colors.primaryVariant
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Statistics",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Row(
//            modifier = Modifier.weight(1f)
        ) {
            Button(
                onClick = { /*TODO*/ },
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colors.primaryVariant
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Game\nOver",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Button(
                onClick = { /*TODO*/ },
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colors.primaryVariant
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Start\nGame",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
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

@Composable
private fun ResponsiveExampleText(
    modifier: Modifier = Modifier,
    text: String,
) {
    ResponsiveText(
        modifier = modifier,
        text = text,
        textStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold)
    )
}