package info.sergeikolinichenko.gameatfifteen.database

import androidx.lifecycle.LiveData
import androidx.room.*
import info.sergeikolinichenko.gameatfifteen.models.ScoreGameDbModel

/** Created by Sergei Kolinichenko on 26.01.2023 at 15:57 (GMT+3) **/

@Dao
interface GameDao {

    @Query("SELECT * FROM scores")
    fun getListScores(): LiveData<List<ScoreGameDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScore(scoreGameDbModel: ScoreGameDbModel)

    @Delete
    suspend fun deleteScore(scoreGameDbModel: ScoreGameDbModel)

    @Query("DELETE FROM scores")
    suspend fun deleteAllScores()
}