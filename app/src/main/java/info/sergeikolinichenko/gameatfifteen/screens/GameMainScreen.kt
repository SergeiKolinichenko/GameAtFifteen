package info.sergeikolinichenko.gameatfifteen.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import info.sergeikolinichenko.gameatfifteen.navigation.AppNavGraph
import info.sergeikolinichenko.gameatfifteen.navigation.Screen
import info.sergeikolinichenko.gameatfifteen.screens.game.compose.GameScreen
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.score.GameScore

/** Created by Sergei Kolinichenko on 24.01.2023 at 19:52 (GMT+3) **/

@Composable
fun GameMainScreen(
    orientation: String,
    viewModel: GameViewModel
) {

    val navHostController = rememberNavController()
    
    AppNavGraph(
        navHostController = navHostController,
        gameScreenContent = { GameScreen(
            orientation = orientation,
            viewModel = viewModel,
            clickButtonStatistics = {
                navHostController.navigate(Screen.Scores.route)
            }
        ) },
        scoresScreenContent = {
            GameScore()
        }
    )

}