package info.sergeikolinichenko.gameatfifteen.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:19 (GMT+3) **/

abstract class GameSharedPreferences {

    companion object {
        private const val SHARED_PREFS_NAME = "game_shared_preferences"
        private var INSTANCE: SharedPreferences? = null
        private val LOCK = Any()

        fun getInstance(application: Application): SharedPreferences {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val sharPref =
                    application.applicationContext.getSharedPreferences(
                        SHARED_PREFS_NAME, Context.MODE_PRIVATE
                    )
                INSTANCE = sharPref
                return sharPref
            }
        }
    }
}