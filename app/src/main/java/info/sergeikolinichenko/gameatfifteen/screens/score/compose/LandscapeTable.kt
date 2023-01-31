package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel
import info.sergeikolinichenko.gameatfifteen.utils.ConvertStoreGameToString.convertToString

/** Created by Sergei Kolinichenko on 25.01.2023 at 20:52 (GMT+3) **/


@Composable
fun LandscapeTable(
    modifier: Modifier = Modifier,
    viewModel: ScoreViewModel
) {

    val scores by viewModel.listScoreGame.observeAsState(listOf())

    Row(
        modifier = modifier
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            items( items = scores, key = {it.timeStamp}) {

                GameScoreCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    score = it.convertToString()
                )

            }

        }

    }

//        LazyVerticalGrid(
//            modifier = modifier,
//            columns = GridCells.Fixed(2)
//        ) {
//
//            items( items = scores.value, key = {it.timeStamp}) {
//
//
//                GameScoreCard(
//                    modifier = Modifier
//                        .padding(horizontal = 4.dp, vertical = 2.dp)
//                        .fillMaxWidth()
//                        .height(40.dp),
//                    score = it.convertToString()
//                )
//            }
//
//        }

}


//@Preview
//@Composable
//fun PreviewThemeLightLandscapeTable() {
//    GameAtFifteenTheme(
//        darkTheme = false
//    ) {
//        LandscapeTable(viewModel = ScoreViewModel())
//    }
//}
//
//@Preview
//@Composable
//fun PreviewThemeDarkLandscapeTable() {
//    GameAtFifteenTheme(
//        darkTheme = true
//    ) {
//        LandscapeTable(viewModel = ScoreViewModel())
//    }
//}