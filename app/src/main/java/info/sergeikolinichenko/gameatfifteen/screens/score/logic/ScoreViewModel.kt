package info.sergeikolinichenko.gameatfifteen.screens.score.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:36 (GMT+3) **/

class ScoreViewModel @Inject constructor(

): ViewModel(){


    private var _orientationScreen = MutableLiveData<Int>()
    val orientationScreen: LiveData<Int> = _orientationScreen

    fun getOrientationScreen(orientation: Int) {
        _orientationScreen.value = orientation
    }
}