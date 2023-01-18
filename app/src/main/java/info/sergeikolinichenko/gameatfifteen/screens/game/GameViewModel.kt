package info.sergeikolinichenko.gameatfifteen.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState.Companion.getGameBoard
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlButtonState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlTextState
import info.sergeikolinichenko.gameatfifteen.utils.TimeUtils.differenceInTime
import kotlinx.coroutines.*

/** Created by Sergei Kolinichenko on 11.01.2023 at 21:37 (GMT+3) **/

class GameViewModel : ViewModel() {

    private var _gameControlButtonState =
        MutableLiveData<GameControlButtonState>(GameControlButtonState.Initial)
    val gameControlButtonState: LiveData<GameControlButtonState> = _gameControlButtonState

    private var _gameControlTextState =
        MutableLiveData<GameControlTextState>(GameControlTextState.Initial)
    val gameControlTextState: LiveData<GameControlTextState> = _gameControlTextState

    private var _gameBoard = MutableLiveData<Array<Array<GameBoardState>>>()
    val gameBoard: LiveData<Array<Array<GameBoardState>>> = _gameBoard

    private var _movesNumber = MutableLiveData(0)
    private var _stateTimer = false

    init {
        initGameScreen()
    }

    private fun initGameScreen() {
        _gameBoard.value = getGameBoard()
        _gameControlTextState.value =
            GameControlTextState.TextTimeElapsed(timeElapsed = INIT_TEXT_FIELDS)
        _gameControlTextState.value =
            GameControlTextState.TextMovesNumber(movesNumber = INIT_TEXT_FIELDS)
    }

    fun getPressedGameControlButton(state: GameControlButtonState) {

        if (state is GameControlButtonState.ButtonStartGame) {
            startGame()
        }
        if (state == GameControlButtonState.ButtonGameOver) {
            gameOver()
        }
        if (state == GameControlButtonState.ButtonStatistics) {

        }
    }

    private fun gameOver() {
        _gameBoard.value = getGameBoard()
        resetMovesNumber()
        setButtonStartGameEnabled()
        stopTimer()
    }

    fun onClickGameButton(number: String) {

        var indexX = 0
        var indexY = 0

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

    private fun startGame() {

        viewModelScope.launch {

            var step = 0
            var stepTime = System.currentTimeMillis()
            val noRepeat = mutableListOf<String>()

            do {

                val x = getEmptyX() + (-1..1).random()
                val y = getEmptyY() + (-1..1).random()

                val xy = "$x$y"

                if (!noRepeat.contains(xy)) {

                    if (x != 3 || y != 3) {

                        if (isMoveValid(x, y)) {

                            step++
                            noRepeat.add(xy)
                            if ((step % 5) == 0) noRepeat.clear()

                            stepTime = System.currentTimeMillis()

                            makeMove(x, y)
                            delay(150)

                        }

                    }
                }

                val timeGape = (System.currentTimeMillis()) - stepTime
                if (timeGape > NUMBER_MOVES_TIME_GAPE) noRepeat.clear()

            } while (step < SET_NUMBER_MOVES)

            setButtonStartGameNotEnabled()
            resetMovesNumber()
            startTimer()
        }
    }

    private fun makeMove(x: Int, y: Int) {

        if (isMoveValid(x, y)) {

            setMovesNumber()

            val emptyX = getEmptyX()
            val emptyY = getEmptyY()

            val board = gameBoard.value ?: getGameBoard()

            board[emptyX][emptyY] = board[x][y]
            board[x][y] = GameBoardState.NoPlate

            _gameBoard.value = board.copyOf()

        }
    }

    private fun isMoveValid(x: Int, y: Int): Boolean {

        val blankX = getEmptyX()
        val blankY = getEmptyY()

        if ((x !in 0..3) or (y !in 0..3)) {
            return false
        }

        if ((((blankX == x - 1) or (blankX == x + 1)) and (blankY == y))
            or (((blankY == y - 1) or (blankY == y + 1)) and (blankX == x))
        ) {
            return true
        }
        return false
    }

    private fun getEmptyX(): Int {
        var indexX = 0
        val board = gameBoard.value ?: getGameBoard()
        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexX = x
            }
        }
        return indexX
    }

    private fun getEmptyY(): Int {
        var indexY = 0
        val board = gameBoard.value ?: getGameBoard()
        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexY = y
            }
        }
        return indexY
    }

    private fun setMovesNumber() {
        _movesNumber.value = _movesNumber.value?.plus(1)
        setTextMovesNumber(_movesNumber.value.toString())
    }

    private fun resetMovesNumber() {
        _movesNumber.value = RESET_MOVES_NUMBER
        setTextMovesNumber(INIT_TEXT_FIELDS)
    }

    private fun setButtonStartGameNotEnabled() {
        _gameControlButtonState.value = GameControlButtonState.ButtonStartGame(enable = false)
    }

    private fun setButtonStartGameEnabled() {
        _gameControlButtonState.value = GameControlButtonState.ButtonStartGame(enable = true)
    }

    private fun setTextTimeElapsed(text: String) {
        _gameControlTextState.value =
            GameControlTextState.TextTimeElapsed(timeElapsed = text)
    }

    private fun setTextMovesNumber(text: String) {
        _gameControlTextState.value =
            GameControlTextState.TextMovesNumber(movesNumber = text)
    }

    private fun gameTimer(stateTimer: Boolean) {
        var time = 0
        _stateTimer = stateTimer

        viewModelScope.launch(Dispatchers.Default) {

            while (_stateTimer) {

                delay(1000)

                time++

                withContext(Dispatchers.Main) {
                    if (_stateTimer) setTextTimeElapsed(time.differenceInTime())
                    else setTextTimeElapsed(INIT_TEXT_FIELDS)
                }
            }
        }
    }

    private fun startTimer() {
        gameTimer(true)
    }

    private fun stopTimer() {
        gameTimer(false)
    }

    companion object {
        private const val INIT_TEXT_FIELDS = ""
        private const val RESET_MOVES_NUMBER = 0
        private const val SET_NUMBER_MOVES = 50
        private const val NUMBER_MOVES_TIME_GAPE = 300
    }
}