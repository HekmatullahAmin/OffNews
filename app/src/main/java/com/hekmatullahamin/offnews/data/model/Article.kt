package com.hekmatullahamin.offnews.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.hekmatullahamin.offnews.data.local.BookmarkedArticleEntity
import java.time.OffsetDateTime

/**
 * Represents an article in the OffNews app.
 * This is the external data layer representation of an article.
 */
data class Article(
    val id: Int = 0,
    val sourceName: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val publishedDate: String = "",
    val isBookmarked: Boolean = false
)

/**
 * Converts an [Article] object to a [BookmarkedArticleEntity] object.
 * This is used to transform the external data layer representation of an article
 * into a local data model suitable for storage in the database.
 */

//TODO: The current implementation directly parses the publishedDate string using OffsetDateTime.parse(). This might throw an exception if the string is not in a valid format.
fun Article.toBookmarkedArticleEntity() = BookmarkedArticleEntity(
    id = id,
    sourceName = sourceName,
    title = title,
    description = description,
    imageUrl = imageUrl,
    publishedDate = OffsetDateTime.parse(publishedDate)
)
