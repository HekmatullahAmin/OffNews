package com.hekmatullahamin.offnews.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hekmatullahamin.offnews.utils.OffsetDateTimeConverter

/**
 * The Room database for the OffNews app.
 */
@Database(
    entities = [CachedArticleEntity::class, BookmarkedArticleEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(OffsetDateTimeConverter::class)
abstract class OffNewsDatabase : RoomDatabase() {
    /**
     * Provides access to the [CachedArticleDao].
     */
    abstract fun cachedArticleDao(): CachedArticleDao
    /**
     * Provides access to the [BookmarkedArticleDao].
     */
    abstract fun bookmarkedArticleDao(): BookmarkedArticleDao
}