package info.sergeikolinichenko.gameatfifteen

import android.app.Application
import info.sergeikolinichenko.gameatfifteen.di.DaggerApplicationComponent

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:34 (GMT+3) **/

class GameApp: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}