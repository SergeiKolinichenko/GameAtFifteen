package info.sergeikolinichenko.gameatfifteen.screens.game.states

/** Created by Sergei Kolinichenko on 12.01.2023 at 17:09 (GMT+3) **/

sealed class GameBoardState(
    val number: String
) {
    object PlateOne: GameBoardState( number = PLATE_ONE )
    object PlateTwo: GameBoardState( number = PLATE_TWO )
    object PlateThree: GameBoardState( number = PLATE_THREE )
    object PlateFour: GameBoardState( number = PLATE_FOUR )
    object PlateFive: GameBoardState( number = PLATE_FIVE )
    object PlateSix: GameBoardState( number = PLATE_SIX )
    object PlateSeven: GameBoardState( number = PLATE_SEVEN )
    object PlateEight: GameBoardState( number = PLATE_EIGHT )
    object PlateNine: GameBoardState( number = PLATE_NINE )
    object PlateTen: GameBoardState( number = PLATE_TEN )
    object PlateEleven: GameBoardState( number = PLATE_ELEVEN )
    object PlateTwelve: GameBoardState( number = PLATE_TWELVE )
    object PlateThirteen: GameBoardState( number = PLATE_THIRTEEN )
    object PlateFourteen: GameBoardState( number = PLATE_FOURTEEN )
    object PlateFifteen: GameBoardState( number = PLATE_FIFTEEN )
    object NoPlate: GameBoardState( number = NO_PLATE )

    companion object{

        val PLATE_LIST = arrayOf(
            arrayOf(PlateOne, PlateTwo, PlateThree, PlateFour),
            arrayOf(PlateFive, PlateSix, PlateSeven, PlateEight),
            arrayOf(PlateNine, PlateTen, PlateEleven, PlateTwelve),
            arrayOf(PlateThirteen, PlateFourteen, PlateFifteen, NoPlate)
        )

        const val PLATE_ONE = "1"
        const val PLATE_TWO = "2"
        const val PLATE_THREE = "3"
        const val PLATE_FOUR = "4"
        const val PLATE_FIVE = "5"
        const val PLATE_SIX = "6"
        const val PLATE_SEVEN = "7"
        const val PLATE_EIGHT = "8"
        const val PLATE_NINE = "9"
        const val PLATE_TEN = "10"
        const val PLATE_ELEVEN = "11"
        const val PLATE_TWELVE = "12"
        const val PLATE_THIRTEEN = "13"
        const val PLATE_FOURTEEN = "14"
        const val PLATE_FIFTEEN = "15"
        const val NO_PLATE = ""
    }
}
