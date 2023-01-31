package info.sergeikolinichenko.gameatfifteen.models

import javax.inject.Inject

/** Created by Sergei Kolinichenko on 19.01.2023 at 20:19 (GMT+3) **/

class MapperGameModels @Inject constructor() {

    fun mapEntityToPrefsModel(stateGame: StateGame) =
        StateGamePrefModel(
            state = stateGame.state,
            moves = stateGame.moves,
            time = stateGame.time
        )

    fun mapPrefsModelToEntity(stateGamePrefs: StateGamePrefModel) =
        StateGame(
            state = stateGamePrefs.state,
            moves = stateGamePrefs.moves,
            time = stateGamePrefs.time
        )

    fun mapEntityToDbModel(scoreGame: ScoreGame) = ScoreGameDbModel(
        timeStamp = scoreGame.timeStamp,
        doneMoves = scoreGame.doneMoves,
        timeUsed = scoreGame.timeUsed
    )

    private fun mapDbModelToEntity(scoreGameDbModel: ScoreGameDbModel) = ScoreGame(
        timeStamp = scoreGameDbModel.timeStamp,
        doneMoves = scoreGameDbModel.doneMoves,
        timeUsed = scoreGameDbModel.timeUsed
    )

    fun mapListDbModelToListEntity(list: List<ScoreGameDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}