package info.sergeikolinichenko.gameatfifteen.models

/** Created by Sergei Kolinichenko on 26.01.2023 at 15:11 (GMT+3) **/

data class ScoreGame(
    val timeStamp: Long,
    val doneMoves: Int,
    val timeUsed: Int
)
