package info.sergeikolinichenko.gameatfifteen.screens.game.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.models.StateGame
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState.Companion.getGameArray
import info.sergeikolinichenko.gameatfifteen.screens.game.states.WinResult
import info.sergeikolinichenko.gameatfifteen.usecases.GetGameUseCase
import info.sergeikolinichenko.gameatfifteen.usecases.GetListScoreGameUseCase
import info.sergeikolinichenko.gameatfifteen.usecases.SetGameUseCase
import info.sergeikolinichenko.gameatfifteen.usecases.SetScoreGameUseCase
import info.sergeikolinichenko.gameatfifteen.utils.ConvertUtils.intToTimeString
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 11.01.2023 at 21:37 (GMT+3) **/

class GameViewModel @Inject constructor(
    private val setGame: SetGameUseCase,
    private val getGame: GetGameUseCase,
    private val setScoreGame: SetScoreGameUseCase,
    getListScoreGame: GetListScoreGameUseCase
) : ViewModel() {

    private var _timeElapsedText = MutableLiveData(RESET_STRING_TO_EMPTY)
    val timeElapsedText: LiveData<String> = _timeElapsedText

    private var _movesNumberText = MutableLiveData(RESET_STRING_TO_EMPTY)
    val movesNumberText: LiveData<String> = _movesNumberText

    private var _gameBoard = MutableLiveData<Array<Array<GameBoardState>>>()
    val gameBoard: LiveData<Array<Array<GameBoardState>>> = _gameBoard

    // Board accepts taps only while a game is in progress
    private var _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    // null - win dialog is hidden
    private var _winResult = MutableLiveData<WinResult?>(null)
    val winResult: LiveData<WinResult?> = _winResult

    private var _movesNumber = RESET_INT_COUNTER_TO_ZERO
    private var _countTimer = RESET_INT_COUNTER_TO_ZERO
    private var timerJob: Job? = null

    // Saved results, to tell the player about a new record
    private val listScoreGame: LiveData<List<ScoreGame>> = getListScoreGame.invoke()
    private var scores: List<ScoreGame> = emptyList()
    private val scoresObserver = Observer<List<ScoreGame>> { list ->
        scores = list.filter { it.timeUsed > 0 }
    }

    init {
        listScoreGame.observeForever(scoresObserver)
        initGameScreen()
    }

    private fun initGameScreen() {
        if (testEnableGameBord()) {
            restoreGame()
        } else {
            _gameBoard.value = getGameArray()
        }
    }

    fun onClickStartGame() {
        startGame()
    }

    fun onClickGiveUp() {
        gameOver()
    }

    fun onClickPlayAgain() {
        setHideWinDialog()
        startGame()
    }

    fun onDismissWinDialog() {
        setHideWinDialog()
    }

    fun onClickGameButton(number: String) {

        if (_isPlaying.value != true) return

        var indexX = -1
        var indexY = -1

        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, gameBoardState ->
                if (gameBoardState.number == number) {
                    indexX = x
                    indexY = y
                }
            }
        }

        if (indexX < 0) return

        // A tap on any tile in the row or column of the empty cell slides
        // the whole chain; each moved tile counts as one move
        val emptyX = getEmptyX()
        val emptyY = getEmptyY()
        when {
            indexX == emptyX && indexY != emptyY -> {
                val step = if (indexY > emptyY) 1 else -1
                var y = emptyY + step
                while (_isPlaying.value == true) {
                    makeMove(indexX, y)
                    if (y == indexY) break
                    y += step
                }
            }

            indexY == emptyY && indexX != emptyX -> {
                val step = if (indexX > emptyX) 1 else -1
                var x = emptyX + step
                while (_isPlaying.value == true) {
                    makeMove(x, indexY)
                    if (x == indexX) break
                    x += step
                }
            }
        }
    }

    // Called when the app goes to the background: the clock must not run
    fun pauseGame() {
        stopTimer()
    }

    fun resumeGame() {
        if (_isPlaying.value == true && _movesNumber > 0) startTimer()
    }

    private fun gameOver() {
        stopTimer()
        _isPlaying.value = false
        _gameBoard.value = getGameArray()
        resetCounters()
        clearSavedGame()
    }

    private fun startGame() {
        stopTimer()
        clearSavedGame()
        _gameBoard.value = getShuffledGameArray()
        resetCounters()
        _timeElapsedText.value = RESET_INT_COUNTER_TO_ZERO.intToTimeString()
        _movesNumberText.value = RESET_INT_COUNTER_TO_ZERO.toString()
        _isPlaying.value = true
    }

    // A random permutation is solvable for a 4x4 board only when
    // inversions + row of the empty cell counted from the bottom is odd
    private fun getShuffledGameArray(): Array<Array<GameBoardState>> {
        val solved = getGameArray().flatten()
        var plates: List<GameBoardState>
        do {
            plates = solved.shuffled()
        } while (!isSolvable(plates) || plates == solved)

        return Array(BOARD_SIZE) { x ->
            Array(BOARD_SIZE) { y -> plates[x * BOARD_SIZE + y] }
        }
    }

    private fun isSolvable(plates: List<GameBoardState>): Boolean {
        val numbers = plates
            .filter { it != GameBoardState.NoPlate }
            .map { it.number.toInt() }

        var inversions = 0
        for (i in numbers.indices) {
            for (j in i + 1 until numbers.size) {
                if (numbers[i] > numbers[j]) inversions++
            }
        }

        val emptyRowFromBottom = BOARD_SIZE - plates.indexOf(GameBoardState.NoPlate) / BOARD_SIZE
        return (inversions + emptyRowFromBottom) % 2 == 1
    }

    private fun makeMove(x: Int, y: Int) {

        if (isMoveValid(x, y)) {

            _movesNumber++
            _movesNumberText.value = _movesNumber.toString()
            // The clock starts with the first move, not with the shuffle
            if (_movesNumber == 1) startTimer()

            val emptyX = getEmptyX()
            val emptyY = getEmptyY()

            val board = gameBoard.value ?: getGameArray()

            board[emptyX][emptyY] = board[x][y]
            board[x][y] = GameBoardState.NoPlate

            _gameBoard.value = board.copyOf()

            if (chekWin()) gameWon()
        }
    }

    private fun gameWon() {
        stopTimer()
        _isPlaying.value = false
        // Compare with the results saved before this game
        val bestMoves = scores.minOfOrNull { it.doneMoves }
        val bestTime = scores.minOfOrNull { it.timeUsed }
        saveScoreGame()
        clearSavedGame()
        _winResult.value = WinResult(
            moves = _movesNumber,
            time = _countTimer.intToTimeString(),
            isRecordMoves = bestMoves != null && _movesNumber < bestMoves,
            isRecordTime = bestTime != null && _countTimer < bestTime,
            isFirstWin = scores.isEmpty()
        )
    }

    private fun saveScoreGame() {
        val scoreGame = ScoreGame(
            timeStamp = System.currentTimeMillis(),
            doneMoves = _movesNumber,
            timeUsed = _countTimer
        )
        viewModelScope.launch {
            setScoreGame.invoke(scoreGame = scoreGame)
        }
    }

    private fun chekWin(): Boolean {
        val board = gameBoard.value ?: getGameArray()
        return (board.contentDeepEquals(getGameArray()))
    }

    private fun setHideWinDialog() {
        _winResult.value = null
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

    private fun resetCounters() {
        _movesNumber = RESET_INT_COUNTER_TO_ZERO
        _countTimer = RESET_INT_COUNTER_TO_ZERO
        _movesNumberText.value = RESET_STRING_TO_EMPTY
        _timeElapsedText.value = RESET_STRING_TO_EMPTY
    }

    // One ticking job at most, so the clock can never run twice as fast
    private fun startTimer() {
        if (timerJob?.isActive == true) return
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(TIMER_TICK)
                _countTimer++
                _timeElapsedText.value = _countTimer.intToTimeString()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun saveGame() {

        if (_isPlaying.value == true && _movesNumber > 0) {
            val stateGame = StateGame(
                state = getGameBoardToJson(),
                time = _countTimer,
                moves = _movesNumber
            )
            setGame(stateGame)
        }
    }

    private fun clearSavedGame() {
        setGame.invoke(
            StateGame(
                state = JSONArray(emptyStringArray()).toString(),
                moves = 0,
                time = 0
            )
        )
    }

    private fun restoreGame() {

        val game = getGame.invoke()
        val board = getJsonToGameBoard(game.state)

        clearSavedGame()

        if (board.contentDeepEquals(getGameArray())) {
            _gameBoard.value = getGameArray()
            return
        }

        _gameBoard.value = board
        _countTimer = game.time
        _movesNumber = game.moves

        _movesNumberText.value = _movesNumber.toString()
        _timeElapsedText.value = _countTimer.intToTimeString()
        _isPlaying.value = true
    }

    private fun getGameBoardToJson(): String {

        val board = gameBoard.value ?: getGameArray()
        val array = emptyStringArray()

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
        val state = getGame.invoke()
        return (state.moves > 0)
    }

    override fun onCleared() {
        stopTimer()
        listScoreGame.removeObserver(scoresObserver)
        super.onCleared()
    }

    companion object {
        private const val RESET_STRING_TO_EMPTY = ""
        private const val RESET_INT_COUNTER_TO_ZERO = 0
        private const val BOARD_SIZE = 4
        private const val TIMER_TICK = 1000L

        private fun emptyStringArray() = arrayOf(
            arrayOf("", "", "", ""),
            arrayOf("", "", "", ""),
            arrayOf("", "", "", ""),
            arrayOf("", "", "", "")
        )
    }
}
