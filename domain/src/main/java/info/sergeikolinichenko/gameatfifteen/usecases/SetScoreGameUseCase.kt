package info.sergeikolinichenko.gameatfifteen.usecases

import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.repository.GameRepository
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 26.01.2023 at 15:26 (GMT+3) **/

class SetScoreGameUseCase@Inject constructor(
    private val repository: GameRepository
) {

    suspend operator fun invoke(scoreGame: ScoreGame) {
        repository.setScoreGame(scoreGame = scoreGame)
    }
}