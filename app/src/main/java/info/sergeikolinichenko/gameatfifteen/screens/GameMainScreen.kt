package info.sergeikolinichenko.gameatfifteen.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import info.sergeikolinichenko.gameatfifteen.navigation.AppNavGraph
import info.sergeikolinichenko.gameatfifteen.navigation.Screen
import info.sergeikolinichenko.gameatfifteen.screens.game.compose.GameScreen
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.score.compose.GameScore
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel

/** Created by Sergei Kolinichenko on 24.01.2023 at 19:52 (GMT+3) **/

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GameMainScreen(
    gameViewModel: GameViewModel,
    scoreViewModel: ScoreViewModel
) {

    val navHostController = rememberAnimatedNavController()
    
    AppNavGraph(
        navHostController = navHostController,
        gameScreenContent = {
            GameScreen(
            viewModel = gameViewModel,
            clickButtonStatistics = {
                navHostController.navigate(Screen.Scores.route)
            }
        ) },
        scoresScreenContent = {
            GameScore(viewModel = scoreViewModel)
        }
    )

}