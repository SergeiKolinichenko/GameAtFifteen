package info.sergeikolinichenko.gameatfifteen.screens.score.compose

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.screens.game.ResponsiveText
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel

/** Created by Sergei Kolinichenko on 10.01.2023 at 18:55 (GMT+3) **/

private const val MAIN_HEIGHT = -270
private const val HALF_HEIGHT = -135
private const val ZERO = 0

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScore(
    viewModel: ScoreViewModel
) {

    val orientation by viewModel.orientationScreen.observeAsState()

    var direction = true
    val offsetY by viewModel.animationState.observeAsState(ZERO.toFloat())

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            // Title
            ScoreScreenTitle(modifier = Modifier.padding(4.dp))

            // --------------------------

            // List of score card
            PortraitTable(
                modifier = Modifier
                    .weight(1f),
                viewModel = viewModel
            )


        }

        GameScoreButtons(
            viewModel = viewModel,
            offsetY = offsetY
        )

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
                        onLongClick = { })
                    .weight(1f)
                    .fillMaxWidth(),
                viewModel = viewModel
            )
        }

        GameScoreButtons(
            viewModel = viewModel,
            offsetY = offsetY
        )

    }

    //---------------------------------------------
    Box(
        modifier = Modifier
            .background(color = colorResource(id = android.R.color.transparent))
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {

        Image(
            modifier = Modifier
                .size(36.dp)
                .offset(y = offsetY.dp)
                .align(Alignment.BottomCenter)
                .pointerInput(Unit) {
                    var positionY = offsetY
                    detectDragGestures { change, dragAmount ->

                        if (direction) {
                            if (positionY <= ZERO && positionY > HALF_HEIGHT) {
                                positionY += dragAmount.y
                            } else if (positionY <= HALF_HEIGHT) {
                                positionY = MAIN_HEIGHT.toFloat()
                                direction = false
                            } else positionY = ZERO.toFloat()

                        } else {
                            if (positionY >= MAIN_HEIGHT && positionY < HALF_HEIGHT) {
                                positionY += dragAmount.y
                            } else if (positionY >= HALF_HEIGHT) {
                                positionY = ZERO.toFloat()
                                direction = true
                            } else positionY = MAIN_HEIGHT.toFloat()
                        }
                        viewModel.setAnimationState(positionY)
                        if (offsetY < MAIN_HEIGHT)
                            viewModel.setAnimationState(MAIN_HEIGHT.toFloat())
                        if (offsetY > ZERO)
                            viewModel.setAnimationState(ZERO.toFloat())
                    }
                },
            painter = painterResource(
                id = if (offsetY < HALF_HEIGHT) R.drawable.chevron_down_arrow
                else R.drawable.chevron_up_arrow
            ),
            contentDescription = stringResource(R.string.open_score_menu)
        )
    }
    //-------------------------------------
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