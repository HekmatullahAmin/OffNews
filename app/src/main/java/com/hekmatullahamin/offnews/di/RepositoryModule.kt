package com.hekmatullahamin.offnews.di

import com.hekmatullahamin.offnews.data.repository.NewsRepository
import com.hekmatullahamin.offnews.data.repository.NewsRepositoryImpl
import com.hekmatullahamin.offnews.utils.ConnectivityManagerNetworkMonitor
import com.hekmatullahamin.offnews.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing repository dependencies.
 * This module binds the concrete implementations of the repositories to their respective interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the [NewsRepositoryImpl] to the [NewsRepository] interface.
     * @param impl The concrete implementation of the [NewsRepository].
     * @return The [NewsRepository] instance.
     */
    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository

    /**
     * Binds the [ConnectivityManagerNetworkMonitor] to the [NetworkMonitor] interface.
     * @param networkMonitor The concrete implementation of the [NetworkMonitor].
     * @return The [NetworkMonitor] instance.
     */
    @Binds
    @Singleton
    abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}