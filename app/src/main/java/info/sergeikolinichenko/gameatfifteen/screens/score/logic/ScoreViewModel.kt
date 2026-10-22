package info.sergeikolinichenko.gameatfifteen.screens.score.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.screens.score.state.ScoreSort
import info.sergeikolinichenko.gameatfifteen.settings.GameSettings
import info.sergeikolinichenko.gameatfifteen.screens.score.state.ScoreSummary
import info.sergeikolinichenko.gameatfifteen.usecases.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:36 (GMT+3) **/

class ScoreViewModel @Inject constructor(
    getListScoreGame: GetListScoreGameUseCase,
    getListScoresSortUseCase: GetListScoresSortUseCase,
    private val deleteScoreGame: DeleteScoreGameUseCase,
    private val deleteAllScoresGame: DeleteAllScoresGameUseCase,
    private val setListScoresSortUseCase: SetListScoresSortUseCase,
    private val setScoreGame: SetScoreGameUseCase,
    private val settings: GameSettings
) : ViewModel() {

    // Earlier versions could save a fake win without playing (0 seconds)
    private val allScores: LiveData<List<ScoreGame>> =
        getListScoreGame.invoke<LiveData<List<ScoreGame>>>().map { list ->
            list.filter { it.timeUsed > 0 }
        }

    private val _sortBy = MutableLiveData(ScoreSort.fromKey(getListScoresSortUseCase.invoke()))
    val sortBy: LiveData<ScoreSort> = _sortBy

    // Records are kept apart for every board size
    private val _boardSize = MutableLiveData(settings.boardSize)
    val boardSize: LiveData<Int> = _boardSize

    val hasScores: LiveData<Boolean> = allScores.map { it.isNotEmpty() }

    // Sizes worth a chip: the ones played, and the one chosen now
    val sizes: LiveData<List<Int>> = MediatorLiveData<List<Int>>().apply {
        val update = {
            value = (allScores.value.orEmpty().map { it.boardSize } + listOfNotNull(_boardSize.value))
                .distinct()
                .sorted()
        }
        addSource(allScores) { update() }
        addSource(_boardSize) { update() }
    }

    private val sizeScores: LiveData<List<ScoreGame>> = MediatorLiveData<List<ScoreGame>>().apply {
        val update = {
            value = allScores.value.orEmpty().filter { it.boardSize == _boardSize.value }
        }
        addSource(allScores) { update() }
        addSource(_boardSize) { update() }
    }

    val scores: LiveData<List<ScoreGame>> = MediatorLiveData<List<ScoreGame>>().apply {
        val update = {
            value = sortScores(sizeScores.value.orEmpty(), _sortBy.value ?: ScoreSort.DATE)
        }
        addSource(sizeScores) { update() }
        addSource(_sortBy) { update() }
    }

    val summary: LiveData<ScoreSummary> = sizeScores.map { list ->
        ScoreSummary(
            games = list.size,
            bestMoves = list.minOfOrNull { it.doneMoves },
            bestTime = list.minOfOrNull { it.timeUsed }
        )
    }

    // The screen opens on the size the player plays now
    fun onOpen() {
        _boardSize.value = settings.boardSize
    }

    fun setBoardSize(size: Int) {
        _boardSize.value = size
    }

    private fun sortScores(list: List<ScoreGame>, sort: ScoreSort): List<ScoreGame> =
        when (sort) {
            ScoreSort.DATE -> list.sortedByDescending { it.timeStamp }
            ScoreSort.MOVES -> list.sortedWith(compareBy({ it.doneMoves }, { it.timeUsed }))
            ScoreSort.TIME -> list.sortedWith(compareBy({ it.timeUsed }, { it.doneMoves }))
        }

    fun setSort(sort: ScoreSort) {
        setListScoresSortUseCase.invoke(sort.key)
        _sortBy.value = sort
    }

    fun deleteScoreGame(item: ScoreGame) {
        viewModelScope.launch {
            deleteScoreGame.invoke(scoreGame = item)
        }
    }

    fun restoreScoreGame(item: ScoreGame) {
        viewModelScope.launch {
            setScoreGame.invoke(scoreGame = item)
        }
    }

    fun deleteAllScores() {
        viewModelScope.launch {
            deleteAllScoresGame.invoke()
        }
    }
}
