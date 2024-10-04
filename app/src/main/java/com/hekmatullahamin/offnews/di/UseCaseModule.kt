package com.hekmatullahamin.offnews.di

import com.hekmatullahamin.offnews.data.repository.NewsRepository
import com.hekmatullahamin.offnews.domain.FetchTopHeadlinesUseCase
import com.hekmatullahamin.offnews.domain.FormatDateUseCase
import com.hekmatullahamin.offnews.domain.GetArticleUseCase
import com.hekmatullahamin.offnews.domain.GetBookmarkedArticlesUseCase
import com.hekmatullahamin.offnews.domain.GetCachedArticlesUseCase
import com.hekmatullahamin.offnews.domain.UpdateBookmarkUseCase
import com.hekmatullahamin.offnews.domain.SelectArticleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing use case dependencies.
 * This module provides instances of the use cases used in the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    /**
     * Provides the [UpdateBookmarkUseCase] instance.
     * @param newsRepository The [NewsRepository] instance.
     * @return The [UpdateBookmarkUseCase] instance.
     */
    @Provides
    @Singleton
    fun provideToggleBookmarkUseCase(
        newsRepository: NewsRepository
    ): UpdateBookmarkUseCase = UpdateBookmarkUseCase(newsRepository)

    /**
     * Provides the [FormatDateUseCase] instance.
     * @return The [FormatDateUseCase] instance.
     */
    @Provides
    @Singleton
    fun provideFormatDateUseCase(): FormatDateUseCase = FormatDateUseCase()

    /**
     * Provides the [SelectArticleUseCase] instance.
     * @return The [SelectArticleUseCase] instance.
     */
//    TODO: the name should change
    @Provides
    @Singleton
    fun provideUpdateSelectedArticleBookmarkUseCase(): SelectArticleUseCase =
        SelectArticleUseCase()

    /**
     * Provides the [GetBookmarkedArticlesUseCase] instance.
     * @param newsRepository The [NewsRepository] instance.
     * @param formatDateUseCase The [FormatDateUseCase] instance.
     * @return The [GetBookmarkedArticlesUseCase] instance.
     */
    @Provides
    @Singleton
    fun provideGetBookmarkedArticlesUseCase(
        newsRepository: NewsRepository,
        formatDateUseCase: FormatDateUseCase
    ): GetBookmarkedArticlesUseCase = GetBookmarkedArticlesUseCase(
        newsRepository = newsRepository,
        formatDateUseCase = formatDateUseCase
    )

    /**
     * Provides the [FetchTopHeadlinesUseCase] instance.
     * @param newsRepository The [NewsRepository] instance.
     * @return The [FetchTopHeadlinesUseCase] instance.
     */
    @Provides
    @Singleton
    fun provideFetchTopHeadlinesUseCase(
        newsRepository: NewsRepository,
        formatDateUseCase: FormatDateUseCase
    ): FetchTopHeadlinesUseCase = FetchTopHeadlinesUseCase(
        newsRepository = newsRepository
    )

    /**
     * Provides the [GetArticleUseCase] instance.
     * @param newsRepository The [NewsRepository] instance.
     * @param formatDateUseCase The [FormatDateUseCase] instance.
     * @return The [GetArticleUseCase] instance.
     */
    @Provides
    @Singleton
    fun provideGetArticleUseCase(
        newsRepository: NewsRepository,
        formatDateUseCase: FormatDateUseCase
    ): GetArticleUseCase = GetArticleUseCase(
        newsRepository = newsRepository,
        formatDateUseCase = formatDateUseCase
    )

    /**
     * Provides the [GetCachedArticlesUseCase] instance.
     * @param newsRepository The [NewsRepository] instance.
     * @param formatDateUseCase The [FormatDateUseCase] instance.
     * @return The [GetCachedArticlesUseCase] instance.
     */
    @Provides
    @Singleton
    fun provideGetCachedArticlesUseCase(
        newsRepository: NewsRepository,
        formatDateUseCase: FormatDateUseCase
    ): GetCachedArticlesUseCase = GetCachedArticlesUseCase(
        newsRepository = newsRepository,
        formatDateUseCase = formatDateUseCase
    )
}