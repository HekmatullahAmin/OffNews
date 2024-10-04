package com.hekmatullahamin.offnews.di

import com.hekmatullahamin.offnews.utils.Dispatcher
import com.hekmatullahamin.offnews.utils.OffNewsDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Hilt module for providing Coroutine dispatchers.
 * This module provides dispatchers for different types of operations,
 * such as I/O and default operations.
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    /**
     * Provides the I/O dispatcher.
     * This dispatcher is optimized for I/O-bound operations, such as network requests and database interactions.
     * @return The I/O dispatcher.
     */
    @Provides
    @Dispatcher(OffNewsDispatcher.IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides the default dispatcher.
     * This dispatcher is optimized for CPU-bound operations, such as heavy computations and data processing.
     * @return The default dispatcher.
     */
    @Provides
    @Dispatcher(OffNewsDispatcher.Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}