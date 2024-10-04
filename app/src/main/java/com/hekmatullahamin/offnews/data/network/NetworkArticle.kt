package com.hekmatullahamin.offnews.data.network

import com.hekmatullahamin.offnews.data.local.CachedArticleEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

/**
 * Represents an article received from the network.
 * This is the network representation of an article.
 */
@Serializable
data class NetworkArticle(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    @SerialName(value = "urlToImage")
    val imageUrl: String?,
    @SerialName(value = "publishedAt")
    val publishedDate: String,
    val content: String?
)

/**
 * Converts a [NetworkArticle] object to a [CachedArticleEntity] object.
 * This is used to transform the network representation of an article
 * into a local data model suitable for storage in the database.
 * @param publishedDate The parsed [OffsetDateTime] representation of the published date.
 * @return A [CachedArticleEntity] object representing the network article.
 */
fun NetworkArticle.toCachedArticleEntity(publishedDate: OffsetDateTime) = CachedArticleEntity(
    sourceName = source.name,
    title = title,
    description = description,
    imageUrl = imageUrl,
    publishedDate = publishedDate
)