package info.sergeikolinichenko.gameatfifteen.screens.score.logic

import androidx.lifecycle.*
import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.screens.score.state.GameScoreButtonState
import info.sergeikolinichenko.gameatfifteen.usecases.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 25.01.2023 at 19:36 (GMT+3) **/

class ScoreViewModel @Inject constructor(
    getListScoreGame: GetListScoreGameUseCase,
    getListScoresSortUseCase: GetListScoresSortUseCase,
    private val deleteScoreGame: DeleteScoreGameUseCase,
    private val deleteAllScoresGame: DeleteAllScoresGameUseCase,
    private val setListScoresSortUseCase: SetListScoresSortUseCase
) : ViewModel() {

    private var _listScoreGame: MutableLiveData<List<ScoreGame>> =
        getListScoreGame.invoke()
    val listScoreGame: LiveData<List<ScoreGame>> =
        sortScoreList(_listScoreGame)


    private var _orientationScreen = MutableLiveData<Int>()
    val orientationScreen: LiveData<Int> = _orientationScreen

    private var _animationState = MutableLiveData(0f)
    val animationState: LiveData<Float> = _animationState

    private var sortBy = when (getListScoresSortUseCase.invoke()) {
        SORT_BY_TIME_TIMER -> SORT_BY_TIME_TIMER
        SORT_BY_TIME_MOVES -> SORT_BY_TIME_MOVES
        else -> SORT_BY_TIME_STAMP
    }

    private fun sortScoreList(
        mList: MutableLiveData<List<ScoreGame>>
    ): LiveData<List<ScoreGame>> {


        val result: LiveData<List<ScoreGame>> =
            Transformations.map(mList) { list ->
                when (sortBy) {
                    SORT_BY_TIME_MOVES -> list.sortedBy { it.doneMoves }
                    SORT_BY_TIME_TIMER -> list.sortedBy { it.timeUsed }
                    else -> list.sortedBy { it.timeStamp }
                }
            }

        return result
    }

    fun handlerButtons(state: GameScoreButtonState) {
        when (state) {
            GameScoreButtonState.ButtonSortByMoves -> {
                setListScoresSortUseCase.invoke(SORT_BY_TIME_MOVES)
                sortBy = SORT_BY_TIME_MOVES
                _listScoreGame.value = _listScoreGame.value
            }
            GameScoreButtonState.ButtonSortByTimer -> {
                setListScoresSortUseCase.invoke(SORT_BY_TIME_TIMER)
                sortBy = SORT_BY_TIME_TIMER
                _listScoreGame.value = _listScoreGame.value
            }
            GameScoreButtonState.ButtonSortByDate -> {
                setListScoresSortUseCase.invoke(SORT_BY_TIME_STAMP)
                sortBy = SORT_BY_TIME_STAMP
                _listScoreGame.value = _listScoreGame.value
            }
            GameScoreButtonState.ButtonDeleteEverything -> {
                viewModelScope.launch {
                    deleteAllScoreGame()
                }
            }
        }
        hideButtons()
    }

    fun getOrientationScreen(orientation: Int) {
        _orientationScreen.value = orientation
    }

    fun deleteScoreGame(item: ScoreGame) {
        viewModelScope.launch {
            deleteScoreGame.invoke(scoreGame = item)
        }
    }

    private suspend fun deleteAllScoreGame() {
        deleteAllScoresGame.invoke()
    }

    fun setAnimationState(state: Float) {
        _animationState.value = state
    }

    fun hideButtons() {
        _animationState.value = 0f
    }

    companion object {
        private const val SORT_BY_TIME_STAMP = "SORT_BY_TIME_STAMP"
        private const val SORT_BY_TIME_TIMER = "SORT_BY_TIME_TIMER"
        private const val SORT_BY_TIME_MOVES = "SORT_BY_TIME_MOVES"
    }
}