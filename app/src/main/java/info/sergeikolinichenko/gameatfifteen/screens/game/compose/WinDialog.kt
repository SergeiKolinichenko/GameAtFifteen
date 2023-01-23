package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
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
import androidx.compose.ui.window.DialogProperties
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 23.01.2023 at 20:28 (GMT+3) **/

@Composable
fun WinDialog(enable: Boolean = false) {

    if (enable) {

        AlertDialog(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            onDismissRequest = {

            },
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Congratulations!\nYou have won!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "You can achieve the best result! \n" +
                            " Start a new game?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    DialogButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        titleText = "Cancel",
                        onClickListener = {

                        }
                    )

                    DialogButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        titleText = "Start",
                        onClickListener = {

                        }
                    )
                }
            }
        )

    }
}

@Composable
private fun DialogButton(
    modifier: Modifier = Modifier,
    titleText: String,
    onClickListener: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = { onClickListener() }
    ) {
        ResponsiveTitleText(text = titleText)
    }
}

@Composable
private fun ResponsiveTitleText(
    modifier: Modifier = Modifier,
    text: String,
) {
    ResponsiveText(
        modifier = modifier,
        text = text,
        textStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
        textAlign = TextAlign.End
    )
}

@Preview
@Composable
fun PreviewThemeLightWinDialog() {
    GameAtFifteenTheme(
        darkTheme = false
    ) {
        WinDialog()
    }
}

@Preview
@Composable
fun PreviewThemeDarkWinDialog() {
    GameAtFifteenTheme(
        darkTheme = true
    ) {
        WinDialog()
    }
}