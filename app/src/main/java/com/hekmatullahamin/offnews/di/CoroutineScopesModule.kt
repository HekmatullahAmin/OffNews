package com.hekmatullahamin.offnews.di

import com.hekmatullahamin.offnews.utils.Dispatcher
import com.hekmatullahamin.offnews.utils.OffNewsDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

/**
 * Hilt module for providing Coroutine scopes.
 * This module provides a Coroutine scope with the application scope,
 * which is tied to the lifecycle of the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutinesScopesModule {

    /**
     * Provides a Coroutine scope with the application scope.
     * @param dispatcher The Coroutine dispatcher to use for the scope.
     * @return A [CoroutineScope] with the application scope.
     */
    @Provides
    @Singleton
    @ApplicationScope
    fun providesCoroutineScope(
        @Dispatcher(OffNewsDispatcher.Default) dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}