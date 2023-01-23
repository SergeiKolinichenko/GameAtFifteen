package info.sergeikolinichenko.gameatfifteen.di

import android.app.Application
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import info.sergeikolinichenko.gameatfifteen.preferences.GameSharedPreferences
import info.sergeikolinichenko.gameatfifteen.repository.GameRepository
import info.sergeikolinichenko.gameatfifteen.repository.GameRepositoryImpl

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:39 (GMT+3) **/

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindNoteRepository(impl: GameRepositoryImpl): GameRepository


    companion object {

        @Provides
        fun provideSharedPreferences(application: Application): SharedPreferences {
            return GameSharedPreferences.getInstance(application)
        }

    }
}