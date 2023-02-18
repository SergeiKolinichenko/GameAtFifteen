package info.sergeikolinichenko.gameatfifteen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import info.sergeikolinichenko.gameatfifteen.navigation.AppNavGraph
import info.sergeikolinichenko.gameatfifteen.navigation.Screen
import info.sergeikolinichenko.gameatfifteen.screens.ViewModelsFactory
import info.sergeikolinichenko.gameatfifteen.screens.game.compose.GameScreen
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.score.compose.GameScore
import info.sergeikolinichenko.gameatfifteen.screens.score.logic.ScoreViewModel
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme
import javax.inject.Inject

class GameActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelsFactory
    private val gameViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }
    private val scoreViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ScoreViewModel::class.java]
    }

    private val component by lazy {
        (application as GameApp).component
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        gameViewModel.getOrientationScreen(resources.configuration.orientation)
        scoreViewModel.getOrientationScreen(resources.configuration.orientation)

        gameViewModel.cancelApp.observe(this) { cancelApp() }

        setContent {

            /*
            Crutch for Xiaomi X3, without it,
            when you rotate the screen to Portrait orientation,
             there was a blank screen.
              Couldn't think of anything better...
             */
            Text(text = " ")

            GameAtFifteenTheme {

                Surface(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.background)
                        .fillMaxSize(),
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
                            )
                        },
                        scoresScreenContent = {
                            GameScore(viewModel = scoreViewModel)
                        }
                    )
                }
            }
        }
    }

    override fun onStop() {
        gameViewModel.saveGame()
        super.onStop()
    }

    private fun cancelApp() {
        finish()
    }
}