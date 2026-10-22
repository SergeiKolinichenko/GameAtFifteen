package info.sergeikolinichenko.gameatfifteen.screens.game.states

/** Created by Sergei Kolinichenko on 12.01.2023 at 17:09 (GMT+3) **/

// One cell of the board: a tile with its number, or the empty cell
class GameBoardState private constructor(
    val number: String
) {

    override fun equals(other: Any?) = other is GameBoardState && other.number == number

    override fun hashCode() = number.hashCode()

    override fun toString() = number.ifEmpty { "_" }

    companion object {

        val NoPlate = GameBoardState(NO_PLATE)

        fun plate(number: Int) = GameBoardState(number.toString())

        fun fromNumber(number: String) = if (number == NO_PLATE) NoPlate else GameBoardState(number)

        // The solved board: 1..size²-1 row by row, the empty cell last
        fun getGameArray(size: Int = DEFAULT_SIZE): Array<Array<GameBoardState>> =
            Array(size) { x ->
                Array(size) { y ->
                    val index = x * size + y
                    if (index == size * size - 1) NoPlate else plate(index + 1)
                }
            }

        const val NO_PLATE = ""
        const val DEFAULT_SIZE = 4
        val SIZES = 3..6
    }
}
