package info.sergeikolinichenko.gameatfifteen.navigation

/** Created by Sergei Kolinichenko on 24.01.2023 at 18:41 (GMT+3) **/

sealed class Screen(
    val route: String
) {

    object Game: Screen(route = SCREEN_GAME)
    object Scores: Screen(route = SCREEN_SCORES)

    companion object{
        private const val SCREEN_GAME = "screen_game"
        private const val SCREEN_SCORES = "screen_scores"
    }
}
