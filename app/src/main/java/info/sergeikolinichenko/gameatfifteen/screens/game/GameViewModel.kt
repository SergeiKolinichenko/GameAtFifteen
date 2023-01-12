package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.GameBoardState.Companion.PLATE_LIST

/** Created by Sergei Kolinichenko on 11.01.2023 at 21:37 (GMT+3) **/

class GameViewModel: ViewModel() {

    private var _gameControlStates = MutableLiveData<GameControlState>(GameControlState.Initial)
    val gameControlStates: LiveData<GameControlState> = _gameControlStates

    private var _gameBoard = MutableLiveData(PLATE_LIST)
    val gameBoard: LiveData<Array<Array<GameBoardState>>> = _gameBoard

    fun changeGameControlState(state: GameControlState) {
        _gameControlStates.value = state
    }
}