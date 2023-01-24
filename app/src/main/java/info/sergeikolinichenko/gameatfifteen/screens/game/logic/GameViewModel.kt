package info.sergeikolinichenko.gameatfifteen.screens.game.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.sergeikolinichenko.gameatfifteen.models.StateGame
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState.Companion.getGameArray
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlButtonState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameControlTextState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.WinDialogButtonState
import info.sergeikolinichenko.gameatfifteen.usecases.RestoreGameUseCase
import info.sergeikolinichenko.gameatfifteen.usecases.SaveGameUseCase
import info.sergeikolinichenko.gameatfifteen.utils.TimeUtils.differenceInTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 11.01.2023 at 21:37 (GMT+3) **/

class GameViewModel @Inject constructor(
    private val saveGameUseCase: SaveGameUseCase,
    private val restoreGameUseCase: RestoreGameUseCase
) : ViewModel() {

    private var _gameControlButtonState =
        MutableLiveData<GameControlButtonState>(GameControlButtonState.InitialButtons)
    val gameControlButtonState: LiveData<GameControlButtonState> = _gameControlButtonState

    private var _gameControlTextState =
        MutableLiveData<GameControlTextState>(GameControlTextState.InitialText)
    val gameControlTextState: LiveData<GameControlTextState> = _gameControlTextState

    private var _gameBoard = MutableLiveData<Array<Array<GameBoardState>>>()
    val gameBoard: LiveData<Array<Array<GameBoardState>>> = _gameBoard

    private var _showWinDialog =
        MutableLiveData<WinDialogButtonState>(WinDialogButtonState.WinDialogInitial)
    val showWinDialog: LiveData<WinDialogButtonState> =
        _showWinDialog

    private var _movesNumber = MutableLiveData(0)
    private var _countTimer = 0
    private var _stateTimer = false

    init {
        initGameScreen()
    }

    private fun initGameScreen() {
        if (testEnableGameBord()) {
            restoreGame()
        } else {
            _gameBoard.value = getGameArray()
            _movesNumber.value = 0
            _countTimer = 0
        }


//        _gameControlTextState.value =
//            GameControlTextState.TextTimeElapsed(timeElapsed = INIT_TEXT_FIELDS)
//        _gameControlTextState.value =
//            GameControlTextState.TextMovesNumber(movesNumber = INIT_TEXT_FIELDS)
    }

    fun getPressedGameControlButton(state: GameControlButtonState) {

        if (state is GameControlButtonState.ButtonStartGame) {
            startGame()
        }
        if (state is GameControlButtonState.ButtonGameOver) {
            gameOver()
        }
        if (state is GameControlButtonState.ButtonStatistics) {

        }
    }

    fun onClickWinDialogButton(state: WinDialogButtonState) {
        when(state) {
            WinDialogButtonState.WinDialogButtonStart -> startGame()
            WinDialogButtonState.WinDialogButtonCancel -> gameOver()
            else -> {}
        }
        setHideWinDialog()
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

    private fun gameOver() {
        _gameBoard.value = getGameArray()
        resetMovesNumber()
        setButtonStateGameOver()
        stopTimer()
    }

    private fun startGame() {

        var step = 0
        var stepTime = System.currentTimeMillis()
        val noRepeat = mutableListOf<String>()

        viewModelScope.launch {

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

                if (longStep(stepTime)) noRepeat.clear()

            } while (step < SET_NUMBER_MOVES)

            setButtonStateStartGame()
            resetMovesNumber()
            startTimer()
        }
    }

    private fun longStep(step: Long): Boolean {
        val timeGape = (System.currentTimeMillis()) - step
        return timeGape > NUMBER_MOVES_TIME_GAPE
    }

    private fun makeMove(x: Int, y: Int) {

        if (isMoveValid(x, y)) {

            _movesNumber.value = _movesNumber.value?.plus(1)
            setMovesNumber()

            val emptyX = getEmptyX()
            val emptyY = getEmptyY()

            val board = gameBoard.value ?: getGameArray()

            board[emptyX][emptyY] = board[x][y]
            board[x][y] = GameBoardState.NoPlate

            _gameBoard.value = board.copyOf()

            if (chekWin()) {
                setShowWinDialog()
            }
        }
    }

    private fun chekWin(): Boolean {
        val board = gameBoard.value ?: getGameArray()
        return (board.contentDeepEquals(getGameArray()))
    }

    private fun setShowWinDialog() {
        _showWinDialog.value = WinDialogButtonState.WinDialogButtonStart
    }

    private fun setHideWinDialog() {
        _showWinDialog.value = WinDialogButtonState.WinDialogInitial
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
        val board = gameBoard.value ?: getGameArray()
        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexX = x
            }
        }
        return indexX
    }

    private fun getEmptyY(): Int {
        var indexY = 0
        val board = gameBoard.value ?: getGameArray()
        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexY = y
            }
        }
        return indexY
    }

    private fun setMovesNumber() {
        setTextMovesNumber(_movesNumber.value.toString())
    }

    private fun resetMovesNumber() {
        _movesNumber.value = RESET_MOVES_NUMBER
        setTextMovesNumber(INIT_TEXT_FIELDS)
    }

    private fun setButtonStateStartGame() {
        _gameControlButtonState.value = GameControlButtonState.ButtonStartGame
    }

    private fun setButtonStateGameOver() {
        _gameControlButtonState.value = GameControlButtonState.ButtonGameOver
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

        _stateTimer = stateTimer

        viewModelScope.launch(Dispatchers.Default) {

            while (_stateTimer) {

                delay(1000)

                _countTimer++

                withContext(Dispatchers.Main) {
                    if (_stateTimer) setTextTimeElapsed(_countTimer.differenceInTime())
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
        _countTimer = 0
    }

    fun saveGame() {

        if (_movesNumber.value != 0) {
            val stateGame = StateGame(
                state = getGameBoardToJson(),
                time = _countTimer,
                moves = _movesNumber.value ?: -1
            )
            saveGameUseCase(stateGame)
        }

    }

    private fun restoreGame() {

        val game = restoreGameUseCase.invoke()

        _gameBoard.value = getJsonToGameBoard(game.state)
        _countTimer = game.time
        _movesNumber.value = game.moves

        setMovesNumber()
        startTimer()
        setButtonStateStartGame()

        val clearSaveGame = StateGame(
            state = JSONArray(EMPTY_STRING_ARRAY_ARRAY).toString(),
            moves = 0,
            time = 0
        )
        saveGameUseCase.invoke(clearSaveGame)

    }

    private fun getGameBoardToJson(): String {

        val board = gameBoard.value ?: getGameArray()
        val array = EMPTY_STRING_ARRAY_ARRAY

        board.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, gameBoardState ->
                array[x][y] = gameBoardState.number
            }
        }

        return JSONArray(array).toString()

    }

    private fun getJsonToGameBoard(state: String): Array<Array<GameBoardState>> {
        val board = getGameArray()
        val jsonArray = JSONArray(state)
        for (x in 0 until jsonArray.length()) {
            for (y in 0 until (jsonArray[x] as JSONArray).length()) {
                getGameArray().forEach { array ->
                    array.forEach {
                        if (it.number == (jsonArray[x] as JSONArray)[y]) {
                            board[x][y] = it
                        }
                    }
                }
            }
        }
        return board
    }

    private fun testEnableGameBord(): Boolean {
        val state = restoreGameUseCase.invoke()
        return (state.moves > 0)
    }

    companion object {
        private const val INIT_TEXT_FIELDS = ""
        private const val RESET_MOVES_NUMBER = 0
        private const val SET_NUMBER_MOVES = 50
        private const val NUMBER_MOVES_TIME_GAPE = 300

        private val EMPTY_STRING_ARRAY_ARRAY = arrayOf(
            arrayOf("", "", "", ""),
            arrayOf("", "", "", ""),
            arrayOf("", "", "", ""),
            arrayOf("", "", "", "")
        )
    }
}