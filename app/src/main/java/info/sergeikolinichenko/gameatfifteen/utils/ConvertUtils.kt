package info.sergeikolinichenko.gameatfifteen.utils

import java.text.SimpleDateFormat
import java.util.*

/** Created by Sergei Kolinichenko on 17.01.2023 at 21:06 (GMT+3) **/

object ConvertUtils {

    fun Int.convertIntToFourDigit() = String.format("%04d", this)

    fun Int.intToTimeString(): String {
        val seconds = this % 60
        val minutes = this / 60 % 60
        val hours = this / 60 / 60 % 24
        return String.format( "%02d:%02d:%02d", hours, minutes, seconds )
    }

    fun Long.getDate(): String {
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        return dateFormat.format(this)
    }

    fun Long.getTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(this)
    }

}