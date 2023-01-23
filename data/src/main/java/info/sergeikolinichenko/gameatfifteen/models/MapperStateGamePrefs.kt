package info.sergeikolinichenko.gameatfifteen.models

import javax.inject.Inject

/** Created by Sergei Kolinichenko on 19.01.2023 at 20:19 (GMT+3) **/

class MapperStateGamePrefs @Inject constructor() {

    fun mapModelToPrefsModel(stateGame: StateGame) =
        StateGamePrefModel(
            state = stateGame.state,
            moves = stateGame.moves,
            time = stateGame.time
        )

    fun mapPrefsModelToModel(stateGamePrefs: StateGamePrefModel) =
        StateGame(
            state = stateGamePrefs.state,
            moves = stateGamePrefs.moves,
            time = stateGamePrefs.time
        )
}