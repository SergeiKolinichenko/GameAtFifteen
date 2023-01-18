package info.sergeikolinichenko.gameatfifteen.utils

import java.text.SimpleDateFormat
import java.util.*

/** Created by Sergei Kolinichenko on 17.01.2023 at 21:06 (GMT+3) **/

object TimeUtils {

    fun Int.differenceInTime(): String {
        val seconds = this % 60
        val minutes = this / 60 % 60
//        val hours = this / 60 / 60 % 24
        return String.format( "%02d:%02d", minutes, seconds )
    }

}