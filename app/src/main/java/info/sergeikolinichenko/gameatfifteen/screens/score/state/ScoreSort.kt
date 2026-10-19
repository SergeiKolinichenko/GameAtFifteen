package info.sergeikolinichenko.gameatfifteen.screens.score.state

// key is what the preferences already store, so the saved choice survives the update
enum class ScoreSort(val key: String) {
    DATE("SORT_BY_TIME_STAMP"),
    MOVES("SORT_BY_TIME_MOVES"),
    TIME("SORT_BY_TIME_TIMER");

    companion object {
        fun fromKey(key: String): ScoreSort = entries.firstOrNull { it.key == key } ?: DATE
    }
}
