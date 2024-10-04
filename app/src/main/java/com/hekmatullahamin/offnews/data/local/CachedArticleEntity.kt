package com.hekmatullahamin.offnews.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hekmatullahamin.offnews.data.model.Article
import java.time.OffsetDateTime

/**
 * Represents a cached article entity in the database.
 */
@Entity(tableName = "cached_articles")
data class CachedArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Make id optional with default value 0
    @ColumnInfo(name = "source_name")
    val sourceName: String,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "published_date")
    val publishedDate: OffsetDateTime,
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean = false
)

/**
 * Converts a [CachedArticleEntity] to an [Article] object.
 * This is used to transform the local data model into a model suitable for use
 * by layers external to the data layer.
 * @param publishedDate The formatted published date string.
 * @return An [Article] object representing the cached article.
 */
fun CachedArticleEntity.toUiArticle(publishedDate: String) = Article(
    id = id,
    sourceName = sourceName,
    title = title,
    description = description ?: "",
    imageUrl = imageUrl,
    publishedDate = publishedDate,
    isBookmarked = isBookmarked
)