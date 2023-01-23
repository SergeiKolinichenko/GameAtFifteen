package info.sergeikolinichenko.gameatfifteen.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import info.sergeikolinichenko.gameatfifteen.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

/** Created by Sergei Kolinichenko on 18.01.2023 at 21:08 (GMT+3) **/

@ApplicationScope
class ViewModelsFactory @Inject constructor(
    private val viewModelProviders:
    @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelProviders[modelClass]?.get() as T
    }

}