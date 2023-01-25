package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel
import info.sergeikolinichenko.gameatfifteen.utils.ResponsiveText

/** Created by Sergei Kolinichenko on 10.01.2023 at 18:55 (GMT+3) **/

@Composable
fun GameScore(
    viewModel: ScoreViewModel
) {

    val orientation by viewModel.orientationScreen.observeAsState()

    if (orientation == Configuration.ORIENTATION_PORTRAIT){
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            // Title
            ScoreScreenTitle(modifier = Modifier.padding(4.dp))

            // List of score card
            PortraitTable(
                modifier = Modifier.weight(1f),
                viewModel = viewModel)

            ScoresListSortButtons(viewModel = viewModel)

        }
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
                    .weight(1f)
                    .fillMaxWidth(),
                viewModel = viewModel
            )

            ScoresListSortButtons(viewModel = viewModel)
        }
    }


}

@Composable
fun ResponsiveGameScoreText(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Bold
) {
    ResponsiveText(
        modifier = modifier,
        text = text,
        textStyle = TextStyle(
            fontSize = 20.sp,
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