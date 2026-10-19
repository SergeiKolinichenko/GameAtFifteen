package info.sergeikolinichenko.gameatfifteen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import info.sergeikolinichenko.gameatfifteen.navigation.AppNavGraph
import info.sergeikolinichenko.gameatfifteen.navigation.Screen
import info.sergeikolinichenko.gameatfifteen.screens.ViewModelsFactory
import info.sergeikolinichenko.gameatfifteen.screens.game.compose.GameScreen
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.score.compose.ScoreScreen
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

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            GameAtFifteenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()

                    AppNavGraph(
                        navHostController = navHostController,
                        gameScreenContent = {
                            GameScreen(
                                viewModel = gameViewModel,
                                onClickStatistics = {
                                    navHostController.navigate(Screen.Scores.route) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        },
                        scoresScreenContent = {
                            ScoreScreen(
                                viewModel = scoreViewModel,
                                onBack = { navHostController.popBackStack() },
                                onPlay = { navHostController.popBackStack() }
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        gameViewModel.resumeGame()
    }

    override fun onStop() {
        gameViewModel.saveGame()
        gameViewModel.pauseGame()
        super.onStop()
    }
}
