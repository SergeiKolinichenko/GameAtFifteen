package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState.Companion.PLATE_ARRAY
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlButtonState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlTextState

/** Created by Sergei Kolinichenko on 11.01.2023 at 21:37 (GMT+3) **/

class GameViewModel: ViewModel() {

    private var _gameControlButtonState = MutableLiveData<GameControlButtonState>(GameControlButtonState.Initial)
    val gameControlButtonState: LiveData<GameControlButtonState> = _gameControlButtonState

    private var _gameControlTextState = MutableLiveData<GameControlTextState>(GameControlTextState.Initial)
    val gameControlTextState: LiveData<GameControlTextState> = _gameControlTextState

    var inGameBoard = MutableLiveData(PLATE_ARRAY)
    private var _gameBoard = MutableLiveData(convertArrayToList(PLATE_ARRAY))
    val gameBoard: LiveData<List<GameBoardState>> = _gameBoard

    private var _movesNumber = MutableLiveData(0)
    val movesNumber: LiveData<Int> = _movesNumber

    init {
        initGameScreen()
    }


    private fun initGameScreen() {
        _gameControlTextState.value =
            GameControlTextState.TextTimeElapsed(timeElapsed = INIT_TEXT_TIME_ELAPSED)
        _gameControlTextState.value =
            GameControlTextState.TextMovesNumber(movesNumber = INIT_TEXT_MOVES_NUMBER)
    }

    fun changeGameControlButtonState(state: GameControlButtonState) {
        if (state == GameControlButtonState.ButtonStartGame) {


        }
        if (state == GameControlButtonState.ButtonGameOver) {

        }
        if (state == GameControlButtonState.ButtonStatistics) {

        }
    }

    fun onClickGameButton(number: String) {

        var indexX = 0
        var indexY = 0

        inGameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
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

        if (isMoveValid(x, y)) {

            val emptyX = getEmptyX()
            val emptyY = getEmptyY()

            val board = inGameBoard.value ?: PLATE_ARRAY

            board[emptyX][emptyY] = board[x][y]
            board[x][y] = GameBoardState.NoPlate

            inGameBoard.value = board
            _gameBoard.value = convertArrayToList(board)

            _movesNumber.value = movesNumber.value?.plus(1)
            _gameControlTextState.value = GameControlTextState.TextMovesNumber(movesNumber = movesNumber.value.toString())
        }
    }

    private fun isMoveValid(x: Int, y: Int): Boolean {

        val blankX = getEmptyX()
        val blankY = getEmptyY()

        if ((x !in 0..3) or (y !in 0..3)) {
            return false
        }

        if ((((blankX == x - 1) or (blankX == x + 1)) and (blankY == y))
            or (((blankY == y - 1) or (blankY == y + 1)) and (blankX == x))) {
            return true
        }
        return false
    }

    private fun getEmptyX(): Int {
        var indexX = 0
        val board = inGameBoard.value ?: PLATE_ARRAY
        inGameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexX = x
            }
        }
        return indexX
    }

    private fun getEmptyY(): Int {
        var indexY = 0
        val board = inGameBoard.value ?: PLATE_ARRAY
        inGameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexY = y
            }
        }
        return indexY
    }

    private fun convertArrayToList(array: Array<Array<GameBoardState>>?): List<GameBoardState> {
        val list = mutableListOf<GameBoardState>()
        array?.forEach { arr ->
            arr.forEach {
                list.add(it)
            }
        }
        return list
    }

    companion object{
        private const val INIT_TEXT_TIME_ELAPSED = "00:00"
        private const val INIT_TEXT_MOVES_NUMBER = "0000"
    }
}