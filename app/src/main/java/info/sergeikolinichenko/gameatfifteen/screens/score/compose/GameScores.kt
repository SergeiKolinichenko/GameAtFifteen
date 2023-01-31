package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.screens.game.ResponsiveText
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel

/** Created by Sergei Kolinichenko on 10.01.2023 at 18:55 (GMT+3) **/

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScore(
    viewModel: ScoreViewModel
) {

    val orientation by viewModel.orientationScreen.observeAsState()

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            // Title
            ScoreScreenTitle(modifier = Modifier.padding(4.dp))

            // --------------------------

            // List of score card
            PortraitTable(
                modifier = Modifier
                    .combinedClickable(
                        onClick = { viewModel.hideButtons() },
                        onLongClick = { viewModel.showButtons() })
                    .weight(1f),
                viewModel = viewModel
            )


        }

        GameScoreButtons(viewModel = viewModel)

    } else {

        Column(modifier = Modifier.fillMaxSize()) {

            // Title
            Row(modifier = Modifier.fillMaxWidth()) {
                ScoreScreenTitle(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, top = 4.dp, end = 4.dp)
                )
                ScoreScreenTitle(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, top = 4.dp, end = 4.dp)
                )
            }

            // List of score card
            LandscapeTable(
                modifier = Modifier
                    .combinedClickable(
                        onClick = { viewModel.hideButtons() },
                        onLongClick = { viewModel.showButtons() })
                    .weight(1f)
                    .fillMaxWidth(),
                viewModel = viewModel
            )
        }
        GameScoreButtons(viewModel = viewModel)
    }
}

@Composable
fun ResponsiveGameScoreText(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSize: TextUnit = 18.sp
) {
    ResponsiveText(
        modifier = modifier,
        text = text,
        textStyle = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    )
}

//@Preview
//@Composable
//fun PreviewThemeLightGameScore() {
//    GameAtFifteenTheme(
//        darkTheme = false
//    ) {
//        GameScore()
//    }
//}
//
//@Preview
//@Composable
//fun PreviewThemeDarkGameScore() {
//    GameAtFifteenTheme(
//        darkTheme = true
//    ) {
//        GameScore()
//    }
//}