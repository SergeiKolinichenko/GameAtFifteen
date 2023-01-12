package info.sergeikolinichenko.gameatfifteen

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import info.sergeikolinichenko.gameatfifteen.screens.game.GameBoard
import info.sergeikolinichenko.gameatfifteen.screens.game.GameControl
import info.sergeikolinichenko.gameatfifteen.screens.game.HorizontalOrientation
import info.sergeikolinichenko.gameatfifteen.screens.game.VerticalOrientation
import info.sergeikolinichenko.gameatfifteen.screens.score.GameScore
import info.sergeikolinichenko.gameatfifteen.ui.theme.GameAtFifteenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            GameAtFifteenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when( resources.configuration.orientation) {
                        Configuration.ORIENTATION_PORTRAIT -> VerticalOrientation()
                        Configuration.ORIENTATION_LANDSCAPE -> HorizontalOrientation()
                        else -> {}
                    }
                }
            }
        }
    }
}