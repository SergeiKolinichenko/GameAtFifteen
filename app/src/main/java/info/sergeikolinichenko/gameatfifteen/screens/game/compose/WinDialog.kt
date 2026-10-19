package info.sergeikolinichenko.gameatfifteen.screens.game.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.screens.game.states.WinResult
import info.sergeikolinichenko.gameatfifteen.ui.theme.NumbersStyle

/** Created by Sergei Kolinichenko on 23.01.2023 at 20:28 (GMT+3) **/

@Composable
fun WinDialog(
    result: WinResult?,
    onPlayAgain: () -> Unit,
    onDismiss: () -> Unit
) {
    if (result == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = stringResource(R.string.win_dialog_congratulations),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ResultValue(
                        title = stringResource(R.string.text_move_number),
                        value = result.moves.toString()
                    )
                    ResultValue(
                        title = stringResource(R.string.text_time_elapsed),
                        value = result.time
                    )
                }

                val badge = when {
                    result.isFirstWin -> stringResource(R.string.win_first)
                    result.isRecordMoves && result.isRecordTime ->
                        stringResource(R.string.win_record_both)
                    result.isRecordMoves -> stringResource(R.string.win_record_moves)
                    result.isRecordTime -> stringResource(R.string.win_record_time)
                    else -> null
                }
                if (badge != null) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.win_dialog_title_button_start))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.win_dialog_title_button_cancel))
            }
        }
    )
}

@Composable
private fun ResultValue(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.merge(NumbersStyle),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
