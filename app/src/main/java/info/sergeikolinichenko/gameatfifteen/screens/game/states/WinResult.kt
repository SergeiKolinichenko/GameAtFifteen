package info.sergeikolinichenko.gameatfifteen.screens.game.states

data class WinResult(
    val moves: Int,
    val time: String,
    val isRecordMoves: Boolean,
    val isRecordTime: Boolean,
    val isFirstWin: Boolean
)
