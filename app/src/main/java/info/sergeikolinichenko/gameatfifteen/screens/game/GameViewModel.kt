package info.sergeikolinichenko.gameatfifteen.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState.Companion.PLATE_LIST
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlButtonState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlTextState

/** Created by Sergei Kolinichenko on 11.01.2023 at 21:37 (GMT+3) **/

class GameViewModel: ViewModel() {

    private var _gameControlButtonState = MutableLiveData<GameControlButtonState>(GameControlButtonState.Initial)
    val gameControlButtonState: LiveData<GameControlButtonState> = _gameControlButtonState

    private var _gameControlTextState = MutableLiveData<GameControlTextState>(GameControlTextState.Initial)
    val gameControlTextState: LiveData<GameControlTextState> = _gameControlTextState

    private var _gameBoard = MutableLiveData(PLATE_LIST)
    val gameBoard: LiveData<Array<Array<GameBoardState>>> = _gameBoard

    private var _textTimeElapsed = MutableLiveData("00:00")
    val textTimeElapsed: LiveData<String> = _textTimeElapsed

    private var _textMoveNumber = MutableLiveData("0000")
    val textMoveNumber: LiveData<String> = _textMoveNumber

    fun changeGameControlButtonState(state: GameControlButtonState) {
        if (state == GameControlButtonState.ButtonStartGame) {
            _textTimeElapsed.value = "01:02"
        }
        if (state == GameControlButtonState.ButtonGameOver) {
            _textMoveNumber.value = "0120"
        }
        if (state == GameControlButtonState.ButtonStatistics) {
            _textTimeElapsed.value = "--:--"
            _textMoveNumber.value = "----"
        }
    }

    fun onClickGameButton(number: String) {

        var indexX: Int = 0
        var indexY: Int = 0

        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, gameBoardState ->
                if (gameBoardState.number == number) {
                    indexX = x
                    indexY = y
                }
            }
        }

        makeMove(indexX, indexY)

    }

    private fun makeMove(x: Int, y: Int) {

        Log.d("MyLog", "makeMove one ${gameBoard.value}")

        val emptyX = getEmptyX()
        val emptyY = getEmptyY()
        val board = gameBoard.value ?: PLATE_LIST
        board[emptyX][emptyY] = board[x][y]
        board[x][y] = GameBoardState.NoPlate
        _gameBoard.value = board
    }

    private fun getEmptyX(): Int {
        var indexX = 0
        val board = gameBoard.value ?: PLATE_LIST
        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexX = x
            }
        }
        return indexX
    }

    private fun getEmptyY(): Int {
        var indexY = 0
        val board = gameBoard.value ?: PLATE_LIST
        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexY = y
            }
        }
        return indexY
    }
}