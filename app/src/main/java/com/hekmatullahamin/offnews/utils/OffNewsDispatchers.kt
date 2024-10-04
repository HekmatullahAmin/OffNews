package com.hekmatullahamin.offnews.utils

import androidx.core.location.LocationRequestCompat.Quality
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val offNewsDispatcher: OffNewsDispatcher)

enum class OffNewsDispatcher {
    Default,
    IO
}