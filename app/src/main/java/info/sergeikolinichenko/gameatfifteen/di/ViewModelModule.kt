package info.sergeikolinichenko.gameatfifteen.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import info.sergeikolinichenko.gameatfifteen.screens.game.logic.GameViewModel

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:47 (GMT+3) **/

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    fun bindGameViewModel(viewModel: GameViewModel): ViewModel
}