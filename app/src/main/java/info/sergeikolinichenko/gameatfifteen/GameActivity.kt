package info.sergeikolinichenko.gameatfifteen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import info.sergeikolinichenko.gameatfifteen.screens.GameMainScreen
import info.sergeikolinichenko.gameatfifteen.screens.ViewModelsFactory
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
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
        super.onCreate(savedInstanceState)
        setContent {

            gameViewModel.cancelApp.observe(this) {
                cancelApp()
            }

            gameViewModel.getOrientationScreen(resources.configuration.orientation)
            scoreViewModel.getOrientationScreen(resources.configuration.orientation)

            GameAtFifteenTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    GameMainScreen(
                        gameViewModel = gameViewModel,
                        scoreViewModel = scoreViewModel
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