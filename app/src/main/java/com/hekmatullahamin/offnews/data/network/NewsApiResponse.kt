package com.hekmatullahamin.offnews.data.network

import kotlinx.serialization.Serializable

/**
 * Represents the response from the news API.
 * This data class encapsulates the status, total results, and a list of [NetworkArticle] objects.
 */
@Serializable
data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NetworkArticle>
)
