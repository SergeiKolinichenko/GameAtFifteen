package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import info.sergeikolinichenko.gameatfifteen.R

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:45 (GMT+3) **/

@Composable
fun ScoreScreenTitle(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.secondary
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ResponsiveGameScoreText(text = stringResource(R.string.game_date_title))
                    ResponsiveGameScoreText(text = stringResource(R.string.game_time_title))
                }

                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ResponsiveGameScoreText(text = stringResource(R.string.game_moves_title))
                    ResponsiveGameScoreText(text = stringResource(R.string.game_taimer_title))
                }
        }

    }
}