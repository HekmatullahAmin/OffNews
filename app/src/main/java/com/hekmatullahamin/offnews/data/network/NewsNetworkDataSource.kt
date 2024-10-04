package com.hekmatullahamin.offnews.data.network

import javax.inject.Inject

/**
 * Data source for fetching news articles from the network.
 */
interface NewsNetworkDataSource {
    /**
     * Searches for news articles based on a query.
     * @param query The search query.
     * @return A [NewsApiResponse] object containing the search results.
     */
    suspend fun searchNews(query: String): NewsApiResponse

    /**
     * Gets the top headlines for a specific category.
     * @param category The news category.
     * @return A [NewsApiResponse] object containing the top headlines.
     */
    suspend fun getTopHeadlines(category: String): NewsApiResponse
}

/**
 * Implementation of the [NewsNetworkDataSource] interface.
 */
class NewsNetworkDataSourceImpl @Inject constructor(
    private val newApiService: NewsApiService
) : NewsNetworkDataSource {
    override suspend fun searchNews(query: String): NewsApiResponse =
        newApiService.searchNews(query)

    override suspend fun getTopHeadlines(category: String): NewsApiResponse =
        newApiService.getTopHeadlines(category)
}