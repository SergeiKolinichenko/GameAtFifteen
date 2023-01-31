package info.sergeikolinichenko.gameatfifteen.usecases

import info.sergeikolinichenko.gameatfifteen.repository.GameRepository
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 26.01.2023 at 15:27 (GMT+3) **/

class GetListScoreGameUseCase@Inject constructor(
    private val repository: GameRepository
) {

    operator fun <T> invoke(): T {
        return repository.getListScoreGame()
    }

}