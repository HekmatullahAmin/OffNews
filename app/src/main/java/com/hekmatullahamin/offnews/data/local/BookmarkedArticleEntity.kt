package com.hekmatullahamin.offnews.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hekmatullahamin.offnews.data.model.Article
import java.time.OffsetDateTime

/**
 * Represents a bookmarked article entity in the database.
 */
@Entity(tableName = "bookmarked_articles")
data class BookmarkedArticleEntity(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "source_name")
    val sourceName: String,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "published_date")
    val publishedDate: OffsetDateTime
)

/**
 * Converts a [BookmarkedArticleEntity] to an [Article] object.
 * This is used to transform the local data model into a model suitable for use
 * by layers external to the data layer.
 * @param publishedDate The formatted published date string.
 * @return An [Article] object representing the bookmarked article.
 */
fun BookmarkedArticleEntity.toUiArticle(publishedDate: String) = Article(
    id = id,
    sourceName = sourceName,
    title = title,
    description = description ?: "",
    imageUrl = imageUrl,
    publishedDate = publishedDate,
    isBookmarked = true
)
