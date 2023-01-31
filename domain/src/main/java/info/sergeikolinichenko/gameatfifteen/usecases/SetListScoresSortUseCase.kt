package info.sergeikolinichenko.gameatfifteen.usecases

import info.sergeikolinichenko.gameatfifteen.repository.GameRepository
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 30.01.2023 at 20:35 (GMT+3) **/

class SetListScoresSortUseCase @Inject constructor(
    private val repository: GameRepository
) {

    operator fun invoke(sort: String) {
        repository.setListScoresSort(sort = sort)
    }
}