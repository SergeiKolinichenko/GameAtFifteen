package info.sergeikolinichenko.gameatfifteen.usecases

import info.sergeikolinichenko.gameatfifteen.models.StateGame
import info.sergeikolinichenko.gameatfifteen.repository.GameRepository
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:26 (GMT+3) **/

class SaveGameUseCase @Inject constructor(
    private val repository: GameRepository
) {

    operator fun invoke(stateGame: StateGame) {
        repository.saveStateGame(stateGame = stateGame)
    }
}