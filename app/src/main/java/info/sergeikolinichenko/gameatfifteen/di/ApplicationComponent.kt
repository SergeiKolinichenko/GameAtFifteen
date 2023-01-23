package info.sergeikolinichenko.gameatfifteen.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import info.sergeikolinichenko.gameatfifteen.GameActivity

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:45 (GMT+3) **/

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: GameActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}