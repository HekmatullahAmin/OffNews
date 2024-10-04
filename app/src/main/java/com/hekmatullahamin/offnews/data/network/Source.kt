package com.hekmatullahamin.offnews.data.network

import kotlinx.serialization.Serializable

/**
 * Represents the source of a news article.
 * This data class encapsulates the ID and name of the news source.
 */
@Serializable
data class Source(
    val id: String?,
    val name: String
)