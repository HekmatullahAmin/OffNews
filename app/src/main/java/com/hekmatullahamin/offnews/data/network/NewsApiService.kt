package com.hekmatullahamin.offnews.data.network

import com.hekmatullahamin.offnews.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API service for fetching news articles.
 */
interface NewsApiService {
    /**
     * Searches for news articles based on a query.
     * @param query The search query.
     * @param apiKey The API key for authentication (defaults to ApiConstants.API_KEY).
     * @return A [NewsApiResponse] containing the search results.
     */
    //    endpoint for searching articles
    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): NewsApiResponse


    /**
     * Fetches top headlines based on a category.
     * @param category The news category (e.g., "business", "technology").
     * @param apiKey The API key for authentication (defaults to ApiConstants.API_KEY).
     * @return A [NewsApiResponse] containing the top headlines.
     */
    //    endpoint for fetching app headlines by category
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): NewsApiResponse
}