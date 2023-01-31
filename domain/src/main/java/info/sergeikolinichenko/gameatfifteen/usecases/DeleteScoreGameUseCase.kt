package info.sergeikolinichenko.gameatfifteen.usecases

import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.repository.GameRepository
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 30.01.2023 at 16:16 (GMT+3) **/

class DeleteScoreGameUseCase @Inject constructor(
    private val repository: GameRepository
) {

    suspend operator fun invoke(scoreGame: ScoreGame) {
        repository.deleteScoreGame(scoreGame = scoreGame)
    }
}