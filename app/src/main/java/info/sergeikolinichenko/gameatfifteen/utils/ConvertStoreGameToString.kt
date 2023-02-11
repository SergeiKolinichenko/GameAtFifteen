package info.sergeikolinichenko.gameatfifteen.utils

import info.sergeikolinichenko.gameatfifteen.models.ScoreGame
import info.sergeikolinichenko.gameatfifteen.utils.ConvertUtils.convertIntToFourDigit
import info.sergeikolinichenko.gameatfifteen.utils.ConvertUtils.intToTimeString
import info.sergeikolinichenko.gameatfifteen.utils.ConvertUtils.getDate
import info.sergeikolinichenko.gameatfifteen.utils.ConvertUtils.getTime

/** Created by Sergei Kolinichenko on 26.01.2023 at 18:47 (GMT+3) **/

object ConvertStoreGameToString {
    fun ScoreGame.convertToString(): String {
        val date = this.timeStamp.getDate()
        val time = this.timeStamp.getTime()
        val move = this.doneMoves.convertIntToFourDigit()
        val timer = this.timeUsed.intToTimeString()

        return "$date  |  $time  |  $move  |  $timer"
    }

}