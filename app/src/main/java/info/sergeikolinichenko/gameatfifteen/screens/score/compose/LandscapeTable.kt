package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel

/** Created by Sergei Kolinichenko on 25.01.2023 at 20:52 (GMT+3) **/

@Composable
fun LandscapeTable(
    modifier: Modifier = Modifier,
    viewModel: ScoreViewModel
) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(2)
        ) {
            repeat(100) {
                item {
                    GameScoreCard()
                }
            }
        }

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