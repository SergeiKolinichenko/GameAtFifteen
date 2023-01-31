package info.sergeikolinichenko.gameatfifteen.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Created by Sergei Kolinichenko on 26.01.2023 at 15:14 (GMT+3) **/

@Entity(tableName = "scores")
data class ScoreGameDbModel(
    @PrimaryKey(autoGenerate = false)
    val timeStamp: Long,
    val doneMoves: Int,
    val timeUsed: Int
)
