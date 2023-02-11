package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel
import info.sergeikolinichenko.gameatfifteen.utils.ConvertStoreGameToString.convertToString

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:53 (GMT+3) **/

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PortraitTable(
    modifier: Modifier = Modifier,
    viewModel: ScoreViewModel
) {

    val scores = viewModel.listScoreGame.observeAsState(listOf())

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        items(items = scores.value, key = { it.timeStamp }) {

            val dismissState = rememberDismissState()

            if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                viewModel.deleteScoreGame(it)
            }

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                directions = setOf(DismissDirection.StartToEnd),
                background = {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                            .padding(horizontal = 5.dp, vertical = 3.dp)
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextDelete()
                    }
                }
            ) {

                GameScoreCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    score = it.convertToString()
                )

            }
        }
    }
}
