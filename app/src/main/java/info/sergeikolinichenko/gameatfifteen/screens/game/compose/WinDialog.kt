package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.WinDialogButtonState
import info.sergeikolinichenko.gameatfifteen.screens.game.ResponsiveText

/** Created by Sergei Kolinichenko on 23.01.2023 at 20:28 (GMT+3) **/

@Composable
fun WinDialog(
    state: WinDialogButtonState,
    viewModel: GameViewModel
) {

    val enable = when(state) {
        WinDialogButtonState.WinDialogInitial -> false
        WinDialogButtonState.WinDialogButtonStart -> true
        WinDialogButtonState.WinDialogButtonCancel -> true
    }

    if (enable) {

        AlertDialog(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            onDismissRequest = {

            },
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.win_dialog_congratulations),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.win_dialog_message),
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
                        state = WinDialogButtonState.WinDialogButtonCancel,
                        onClickListener = {
                            viewModel.onClickWinDialogButton(it)
                        }
                    )

                    DialogButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        state = WinDialogButtonState.WinDialogButtonStart,
                        onClickListener = {
                            viewModel.onClickWinDialogButton(it)
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
    state: WinDialogButtonState,
    onClickListener: (state: WinDialogButtonState) -> Unit
) {

    Button(
        modifier = modifier,
        onClick = { onClickListener(state) }
    ) {
        ResponsiveTitleText(text = stringResource(id = state.titleButton))
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

//@Preview
//@Composable
//fun PreviewThemeLightWinDialog() {
//    GameAtFifteenTheme(
//        darkTheme = false
//    ) {
//        WinDialog()
//    }
//}
//
//@Preview
//@Composable
//fun PreviewThemeDarkWinDialog() {
//    GameAtFifteenTheme(
//        darkTheme = true
//    ) {
//        WinDialog()
//    }
//}