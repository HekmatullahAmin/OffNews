package com.hekmatullahamin.offnews.di

import android.content.Context
import androidx.room.Room
import com.hekmatullahamin.offnews.data.local.BookmarkedArticleDao
import com.hekmatullahamin.offnews.data.local.BookmarkedArticlesLocalDataSource
import com.hekmatullahamin.offnews.data.local.BookmarkedArticlesLocalDataSourceImpl
import com.hekmatullahamin.offnews.data.local.CachedArticleDao
import com.hekmatullahamin.offnews.data.local.CachedArticlesLocalDataSource
import com.hekmatullahamin.offnews.data.local.CachedArticlesLocalDataSourceImpl
import com.hekmatullahamin.offnews.data.local.OffNewsDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies.
 * This module provides the Room database instance and DAOs for accessing the database.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the Room database instance.
     * @param context The application context.
     * @return The [OffNewsDatabase] instance.
     */
    @Provides
    @Singleton
    fun provideOffNewsDatabase(@ApplicationContext context: Context): OffNewsDatabase {
        return Room.databaseBuilder(
            context,
            OffNewsDatabase::class.java,
            "off_news_db"
        ).build()
    }

    /**
     * Provides the [CachedArticleDao] instance.
     * @param database The [OffNewsDatabase] instance.
     * @return The [CachedArticleDao] instance.
     */
    @Provides
    @Singleton
    fun provideCachedArticleDao(
        database: OffNewsDatabase
    ): CachedArticleDao = database.cachedArticleDao()

    /**
     * Provides the [BookmarkedArticleDao] instance.
     * @param database The [OffNewsDatabase] instance.
     * @return The [BookmarkedArticleDao] instance.
     */
    @Provides
    @Singleton
    fun provideBookmarkedArticleDao(
        database: OffNewsDatabase
    ): BookmarkedArticleDao = database.bookmarkedArticleDao()
}