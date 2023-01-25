package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:57 (GMT+3) **/

@Composable
fun ScoresListSortButtons(
    viewModel: ScoreViewModel
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            ResponsiveGameScoreText(
                text = "Sort by Movies",
                fontWeight = FontWeight.W900
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            ResponsiveGameScoreText(
                text = "Sort by Timers",
                fontWeight = FontWeight.W900
            )
        }
    }

}