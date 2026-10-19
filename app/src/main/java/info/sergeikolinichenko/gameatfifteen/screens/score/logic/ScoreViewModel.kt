package info.sergeikolinichenko.gameatfifteen.screens.score.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.screens.score.state.ScoreSort
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
    private val setScoreGame: SetScoreGameUseCase
) : ViewModel() {

    // Earlier versions could save a fake win without playing (0 seconds)
    private val allScores: LiveData<List<ScoreGame>> =
        getListScoreGame.invoke<LiveData<List<ScoreGame>>>().map { list ->
            list.filter { it.timeUsed > 0 }
        }

    private val _sortBy = MutableLiveData(ScoreSort.fromKey(getListScoresSortUseCase.invoke()))
    val sortBy: LiveData<ScoreSort> = _sortBy

    val scores: LiveData<List<ScoreGame>> = MediatorLiveData<List<ScoreGame>>().apply {
        val update = {
            value = sortScores(allScores.value.orEmpty(), _sortBy.value ?: ScoreSort.DATE)
        }
        addSource(allScores) { update() }
        addSource(_sortBy) { update() }
    }

    val summary: LiveData<ScoreSummary> = allScores.map { list ->
        ScoreSummary(
            games = list.size,
            bestMoves = list.minOfOrNull { it.doneMoves },
            bestTime = list.minOfOrNull { it.timeUsed }
        )
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
