package info.sergeikolinichenko.gameatfifteen.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/** Created by Sergei Kolinichenko on 17.01.2023 at 21:06 (GMT+3) **/

object ConvertUtils {

    // 0:42, 3:12, 1:05:09
    fun Int.intToTimeString(): String {
        val seconds = this % 60
        val minutes = this / 60 % 60
        val hours = this / 60 / 60
        return if (hours > 0) String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds)
        else String.format(Locale.ROOT, "%d:%02d", minutes, seconds)
    }

    // "Today", "Yesterday" or "19 Oct", localized by the system
    fun Long.getDay(): String {
        return if (DateUtils.isToday(this) || DateUtils.isToday(this + DateUtils.DAY_IN_MILLIS)) {
            DateUtils.getRelativeTimeSpanString(
                this, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS
            ).toString()
        } else {
            SimpleDateFormat("d MMM", Locale.getDefault()).format(this)
        }
    }

    fun Long.getTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(this)
    }

}
