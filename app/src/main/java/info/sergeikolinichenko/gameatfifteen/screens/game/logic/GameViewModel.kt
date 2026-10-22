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
import info.sergeikolinichenko.gameatfifteen.settings.GameSettings
import info.sergeikolinichenko.gameatfifteen.ui.skins.TileSkin
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
    getListScoreGame: GetListScoreGameUseCase,
    private val settings: GameSettings
) : ViewModel() {

    private var _tileSkin = MutableLiveData(settings.skin)
    val tileSkin: LiveData<TileSkin> = _tileSkin

    // Side of the board: 3 for 3x3 and so on
    private var _boardSize = MutableLiveData(settings.boardSize)
    val boardSize: LiveData<Int> = _boardSize
    private val size: Int get() = _boardSize.value ?: GameBoardState.DEFAULT_SIZE

    // Paused game: the clock stands and the board is hidden, so nobody thinks without the clock
    private var _isPaused = MutableLiveData(false)
    val isPaused: LiveData<Boolean> = _isPaused

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
            _gameBoard.value = getGameArray(size)
        }
    }

    fun onSelectSkin(skin: TileSkin) {
        settings.skin = skin
        _tileSkin.value = skin
    }

    // The size can be changed only between games
    fun onSelectBoardSize(boardSize: Int) {
        if (_isPlaying.value == true || boardSize !in GameBoardState.SIZES) return
        settings.boardSize = boardSize
        _boardSize.value = boardSize
        _gameBoard.value = getGameArray(boardSize)
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

        if (_isPlaying.value != true || _isPaused.value == true) return

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

    // The app goes to the background, the statistics open or the player taps pause.
    // Before the first move the clock does not run, so there is nothing to pause.
    fun pauseGame() {
        if (_isPlaying.value != true || _movesNumber == 0) return
        stopTimer()
        _isPaused.value = true
    }

    fun resumeGame() {
        _isPaused.value = false
        if (_isPlaying.value == true && _movesNumber > 0) startTimer()
    }

    private fun gameOver() {
        stopTimer()
        _isPlaying.value = false
        _isPaused.value = false
        _gameBoard.value = getGameArray(size)
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
        _isPaused.value = false
        _isPlaying.value = true
    }

    private fun getShuffledGameArray(): Array<Array<GameBoardState>> {
        val side = size
        val solved = getGameArray(side).flatten()
        var plates: List<GameBoardState>
        do {
            plates = solved.shuffled()
        } while (!isSolvable(plates, side) || plates == solved)

        return Array(side) { x ->
            Array(side) { y -> plates[x * side + y] }
        }
    }

    // Odd side: solvable when the number of inversions is even.
    // Even side: when inversions + row of the empty cell counted from the bottom is odd.
    private fun isSolvable(plates: List<GameBoardState>, size: Int): Boolean {
        val numbers = plates
            .filter { it != GameBoardState.NoPlate }
            .map { it.number.toInt() }

        var inversions = 0
        for (i in numbers.indices) {
            for (j in i + 1 until numbers.size) {
                if (numbers[i] > numbers[j]) inversions++
            }
        }

        if (size % 2 == 1) return inversions % 2 == 0
        val emptyRowFromBottom = size - plates.indexOf(GameBoardState.NoPlate) / size
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

            // Work on a deep copy so the board already on screen is never mutated
            val current = gameBoard.value ?: getGameArray(size)
            val board = Array(current.size) { current[it].copyOf() }

            board[emptyX][emptyY] = board[x][y]
            board[x][y] = GameBoardState.NoPlate

            _gameBoard.value = board

            if (chekWin()) gameWon()
        }
    }

    private fun gameWon() {
        stopTimer()
        _isPlaying.value = false
        // Compare with the results saved before this game, on a board of the same size
        val sameSize = scores.filter { it.boardSize == size }
        val bestMoves = sameSize.minOfOrNull { it.doneMoves }
        val bestTime = sameSize.minOfOrNull { it.timeUsed }
        saveScoreGame()
        clearSavedGame()
        _winResult.value = WinResult(
            moves = _movesNumber,
            time = _countTimer.intToTimeString(),
            isRecordMoves = bestMoves != null && _movesNumber < bestMoves,
            isRecordTime = bestTime != null && _countTimer < bestTime,
            isFirstWin = sameSize.isEmpty()
        )
    }

    private fun saveScoreGame() {
        val scoreGame = ScoreGame(
            timeStamp = System.currentTimeMillis(),
            doneMoves = _movesNumber,
            timeUsed = _countTimer,
            boardSize = size
        )
        viewModelScope.launch {
            setScoreGame.invoke(scoreGame = scoreGame)
        }
    }

    private fun chekWin(): Boolean {
        val board = gameBoard.value ?: getGameArray(size)
        return (board.contentDeepEquals(getGameArray(size)))
    }

    private fun setHideWinDialog() {
        _winResult.value = null
    }

    private fun isMoveValid(x: Int, y: Int): Boolean {

        val blankX = getEmptyX()
        val blankY = getEmptyY()

        if ((x !in 0 until size) or (y !in 0 until size)) {
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
        val board = gameBoard.value ?: getGameArray(size)
        gameBoard.value?.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (board[x][y] == GameBoardState.NoPlate) indexX = x
            }
        }
        return indexX
    }

    private fun getEmptyY(): Int {
        var indexY = 0
        val board = gameBoard.value ?: getGameArray(size)
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
                state = JSONArray().toString(),
                moves = 0,
                time = 0
            )
        )
    }

    private fun restoreGame() {

        val game = getGame.invoke()
        val board = getJsonToGameBoard(game.state)

        clearSavedGame()

        // The saved board tells its own size
        val restoredSize = board?.size ?: size
        _boardSize.value = restoredSize
        settings.boardSize = restoredSize

        if (board == null || board.contentDeepEquals(getGameArray(restoredSize))) {
            _gameBoard.value = getGameArray(restoredSize)
            return
        }

        _gameBoard.value = board
        _countTimer = game.time
        _movesNumber = game.moves

        _movesNumberText.value = _movesNumber.toString()
        _timeElapsedText.value = _countTimer.intToTimeString()
        _isPlaying.value = true
        // Back after the app was closed: the player starts the clock again
        _isPaused.value = true
    }

    private fun getGameBoardToJson(): String {
        val board = gameBoard.value ?: getGameArray(size)
        val array = board.map { row -> row.map { it.number } }
        return JSONArray(array).toString()
    }

    // null when the saved text is not a whole square board of a known size
    private fun getJsonToGameBoard(state: String): Array<Array<GameBoardState>>? {
        val numbers = runCatching {
            val rows = JSONArray(state)
            List(rows.length()) { x ->
                val row = rows.getJSONArray(x)
                List(row.length()) { y -> row.getString(y) }
            }
        }.getOrNull() ?: return null

        val side = numbers.size
        if (side !in GameBoardState.SIZES || numbers.any { it.size != side }) return null
        val board = Array(side) { x ->
            Array(side) { y -> GameBoardState.fromNumber(numbers[x][y]) }
        }
        if (board.flatten().toSet() != getGameArray(side).flatten().toSet()) return null
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
        private const val TIMER_TICK = 1000L
    }
}
