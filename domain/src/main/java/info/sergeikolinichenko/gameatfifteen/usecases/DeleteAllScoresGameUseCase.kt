package info.sergeikolinichenko.gameatfifteen.usecases

import info.sergeikolinichenko.gameatfifteen.repository.GameRepository
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 30.01.2023 at 16:17 (GMT+3) **/

class DeleteAllScoresGameUseCase @Inject constructor(
    private val repository: GameRepository
) {

    suspend operator fun invoke() {
        repository.deleteAllScoresGame()
    }
}