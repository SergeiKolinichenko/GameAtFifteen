package info.sergeikolinichenko.gameatfifteen.screens.game.logic

import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState
import info.sergeikolinichenko.gameatfifteen.screens.game.states.GameBoardState.Companion.getGameArray
import kotlinx.coroutines.*
import javax.inject.Inject

/** Created by Sergei Kolinichenko on 23.01.2023 at 15:10 (GMT+3) **/

class Game @Inject constructor() {

    private val gameArray = getGameArray()

    private val mixPlatesScope = CoroutineScope(Dispatchers.Main)
    private val movePlateScope = CoroutineScope(Dispatchers.Default)

    fun getBardGame(): Array<Array<GameBoardState>> = gameArray

    fun mixPlates() {

        var step = 0
        var stepTime = System.currentTimeMillis()
        val noRepeat = mutableListOf<String>()

        mixPlatesScope.launch {

            do {

                val x = getEmptyX() + (-1..1).random()
                val y = getEmptyY() + (-1..1).random()

                val xy = "$x$y"

                if (!noRepeat.contains(xy)) {

                    if (x != 3 || y != 3) {

                        movePlateScope.launch {

                            if (isMoveValid(x, y)) {

                                step++
                                noRepeat.add(xy)
                                if ((step % 5) == 0) noRepeat.clear()

                                stepTime = movePlate(x, y)

                            }

                        }

                    }
                }

                val timeGape = (System.currentTimeMillis()) - stepTime
                if (timeGape > NUMBER_MOVES_TIME_GAPE) noRepeat.clear()

            } while (step < SET_NUMBER_MOVES)
        }
    }

    private suspend fun movePlate(x: Int, y: Int): Long {

        return withContext(mixPlatesScope.coroutineContext) {

            makeMove(x, y)
            delay(150)

            System.currentTimeMillis()

        }
    }

    private fun isMoveValid(x: Int, y: Int): Boolean {

        val blankX = getEmptyX()
        val blankY = getEmptyY()

        if ((x !in 0..3) or (y !in 0..3)) {
            return false
        }

        if ((((blankX == x - 1) or (blankX == x + 1)) and (blankY == y))
            or (((blankY == y - 1) or (blankY == y + 1)) and (blankX == x))
        ) {
            return true
        }
        return false
    }

    private fun makeMove(x: Int, y: Int) {

        if (isMoveValid(x, y)) {

            val emptyX = getEmptyX()
            val emptyY = getEmptyY()

            gameArray[emptyX][emptyY] = gameArray[x][y]
            gameArray[x][y] = GameBoardState.NoPlate

        }
    }

    private fun getEmptyX(): Int {
        var indexX = 0

        gameArray.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (gameArray[x][y] == GameBoardState.NoPlate) indexX = x
            }
        }
        return indexX
    }

    private fun getEmptyY(): Int {
        var indexY = 0

        gameArray.forEachIndexed { x, arrayOfGameBoardStates ->
            arrayOfGameBoardStates.forEachIndexed { y, _ ->
                if (gameArray[x][y] == GameBoardState.NoPlate) indexY = y
            }
        }
        return indexY
    }

    companion object {
        private const val SET_NUMBER_MOVES = 50
        private const val NUMBER_MOVES_TIME_GAPE = 300
    }
}