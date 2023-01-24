package info.sergeikolinichenko.gameatfifteen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/** Created by Sergei Kolinichenko on 24.01.2023 at 19:01 (GMT+3) **/

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    gameScreenContent: @Composable () -> Unit,
    scoresScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Game.route
    ) {
        composable(route = Screen.Game.route) {
            gameScreenContent()
        }
        composable(route = Screen.Scores.route) {
            scoresScreenContent()
        }
    }
}