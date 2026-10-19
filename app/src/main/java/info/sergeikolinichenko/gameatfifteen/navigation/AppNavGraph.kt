package info.sergeikolinichenko.gameatfifteen.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/** Created by Sergei Kolinichenko on 24.01.2023 at 19:01 (GMT+3) **/

private const val TRANSITION_MS = 300

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    gameScreenContent: @Composable () -> Unit,
    scoresScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Game.route,
        enterTransition = {
            slideIntoContainer(SlideDirection.Start, tween(TRANSITION_MS))
        },
        exitTransition = {
            slideOutOfContainer(SlideDirection.Start, tween(TRANSITION_MS))
        },
        popEnterTransition = {
            slideIntoContainer(SlideDirection.End, tween(TRANSITION_MS))
        },
        popExitTransition = {
            slideOutOfContainer(SlideDirection.End, tween(TRANSITION_MS))
        }
    ) {
        composable(route = Screen.Game.route) {
            gameScreenContent()
        }
        composable(route = Screen.Scores.route) {
            scoresScreenContent()
        }
    }
}
