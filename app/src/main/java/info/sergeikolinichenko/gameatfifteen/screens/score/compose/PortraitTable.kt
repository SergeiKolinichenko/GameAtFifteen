package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:53 (GMT+3) **/

@Composable
fun PortraitTable(
    modifier: Modifier = Modifier,
    viewModel: ScoreViewModel
) {
    LazyColumn(
        modifier = modifier
    ) {

        repeat(500) {
            item {
                GameScoreCard()
            }

        }
    }
}