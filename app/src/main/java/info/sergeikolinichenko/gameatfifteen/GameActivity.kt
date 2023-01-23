package info.sergeikolinichenko.gameatfifteen

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import info.sergeikolinichenko.gameatfifteen.screens.ViewModelsFactory
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.compose.HorizontalOrientation
import info.sergeikolinichenko.gameatfifteen.screens.game.compose.VerticalOrientation
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme
import javax.inject.Inject

class GameActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelsFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val component by lazy {
        (application as GameApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {

            GameAtFifteenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when( resources.configuration.orientation) {
                        Configuration.ORIENTATION_PORTRAIT ->
                            VerticalOrientation(viewModel = viewModel)
                        Configuration.ORIENTATION_LANDSCAPE ->
                            HorizontalOrientation(viewModel = viewModel)
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onStop() {
        viewModel.saveGame()
        super.onStop()
    }
}