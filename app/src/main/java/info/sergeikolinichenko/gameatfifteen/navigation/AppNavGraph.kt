package info.sergeikolinichenko.gameatfifteen.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

/** Created by Sergei Kolinichenko on 24.01.2023 at 19:01 (GMT+3) **/

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    gameScreenContent: @Composable () -> Unit,
    scoresScreenContent: @Composable () -> Unit
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = Screen.Game.route
    ) {
        composable(
            route = Screen.Game.route,
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            }

        ) {
            gameScreenContent()
        }
        composable(route = Screen.Scores.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
            }
        ) {
            scoresScreenContent()
        }
    }
}