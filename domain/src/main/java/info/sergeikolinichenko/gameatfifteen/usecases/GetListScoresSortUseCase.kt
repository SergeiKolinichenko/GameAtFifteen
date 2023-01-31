package info.sergeikolinichenko.gameatfifteen.usecases

import info.sergeikolinichenko.gameatfifteen.repository.GameRepository
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 30.01.2023 at 20:36 (GMT+3) **/

class GetListScoresSortUseCase @Inject constructor(
    private val repository: GameRepository
) {
    operator fun invoke() = repository.getListScoresSort()
}