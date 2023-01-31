package info.sergeikolinichenko.gameatfifteen.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import info.sergeikolinichenko.gameatfifteen.R
import info.sergeikolinichenko.gameatfifteen.navigation.AppNavGraph
import info.sergeikolinichenko.gameatfifteen.navigation.Screen
import info.sergeikolinichenko.gameatfifteen.screens.game.compose.GameScreen
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.score.compose.GameScore
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel

/** Created by Sergei Kolinichenko on 24.01.2023 at 19:52 (GMT+3) **/

@Composable
fun GameMainScreen(
    gameViewModel: GameViewModel,
    scoreViewModel: ScoreViewModel
) {

    val navHostController = rememberNavController()
    
    AppNavGraph(
        navHostController = navHostController,
        gameScreenContent = {
            GameScreen(
            viewModel = gameViewModel,
            clickButtonStatistics = {
                navHostController.navigate(Screen.Scores.route){
                    anim {
                        enter = R.anim.enter_from_right
                        exit = R.anim.exit_to_left
                    }
                }
            }
        ) },
        scoresScreenContent = {
            GameScore(viewModel = scoreViewModel)
        }
    )

}