package info.sergeikolinichenko.gameatfifteen.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/** Created by Sergei Kolinichenko on 18.01.2023 at 20:37 (GMT+3) **/

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)
