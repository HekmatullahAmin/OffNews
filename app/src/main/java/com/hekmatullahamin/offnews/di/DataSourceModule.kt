package com.hekmatullahamin.offnews.di

import com.hekmatullahamin.offnews.data.local.BookmarkedArticlesLocalDataSource
import com.hekmatullahamin.offnews.data.local.BookmarkedArticlesLocalDataSourceImpl
import com.hekmatullahamin.offnews.data.local.CachedArticlesLocalDataSource
import com.hekmatullahamin.offnews.data.local.CachedArticlesLocalDataSourceImpl
import com.hekmatullahamin.offnews.data.network.NewsNetworkDataSource
import com.hekmatullahamin.offnews.data.network.NewsNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing data source dependencies.
 * This module binds the concrete implementations of the data sources to their respective interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    /**
     * Binds the [BookmarkedArticlesLocalDataSourceImpl] to the [BookmarkedArticlesLocalDataSource] interface.
     * @param impl The concrete implementation of the [BookmarkedArticlesLocalDataSource].
     * @return The [BookmarkedArticlesLocalDataSource] instance.
     */
    @Binds
    @Singleton
    abstract fun bindBookmarkedArticlesLocalDataSource(
        impl: BookmarkedArticlesLocalDataSourceImpl
    ): BookmarkedArticlesLocalDataSource

    /**
     * Binds the [CachedArticlesLocalDataSourceImpl] to the [CachedArticlesLocalDataSource] interface.
     * @param impl The concrete implementation of the [CachedArticlesLocalDataSource].
     * @return The [CachedArticlesLocalDataSource] instance.
     */
    @Binds
    @Singleton
    abstract fun bindCachedArticlesLocalDataSource(
        impl: CachedArticlesLocalDataSourceImpl
    ): CachedArticlesLocalDataSource

    /**
     * Binds the [NewsNetworkDataSourceImpl] to the [NewsNetworkDataSource] interface.
     * @param impl The concrete implementation of the [NewsNetworkDataSource].
     * @return The [NewsNetworkDataSource] instance.
     */
    @Binds
    @Singleton
    abstract fun bindNewsNetworkDataSource(
        impl: NewsNetworkDataSourceImpl
    ): NewsNetworkDataSource
}