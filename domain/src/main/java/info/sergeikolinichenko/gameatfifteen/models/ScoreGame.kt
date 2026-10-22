package info.sergeikolinichenko.gameatfifteen.models

/** Created by Sergei Kolinichenko on 26.01.2023 at 15:11 (GMT+3) **/

data class ScoreGame(
    val timeStamp: Long,
    val doneMoves: Int,
    val timeUsed: Int,
    // Side of the board: 3 for 3x3 and so on. Results before 1.7 were all 4x4
    val boardSize: Int = 4
)
