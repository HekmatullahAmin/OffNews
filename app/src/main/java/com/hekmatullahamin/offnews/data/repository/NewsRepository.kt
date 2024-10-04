package com.hekmatullahamin.offnews.data.repository

import android.util.Log
import com.hekmatullahamin.offnews.data.exceptions.NoNetworkException
import com.hekmatullahamin.offnews.data.local.BookmarkedArticleEntity
import com.hekmatullahamin.offnews.data.local.BookmarkedArticlesLocalDataSource
import com.hekmatullahamin.offnews.data.local.CachedArticleEntity
import com.hekmatullahamin.offnews.data.local.CachedArticlesLocalDataSource
import com.hekmatullahamin.offnews.data.network.NewsNetworkDataSource
import com.hekmatullahamin.offnews.data.network.toCachedArticleEntity
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.data.model.toBookmarkedArticleEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime
import javax.inject.Inject


/**
 * Repository for managing news articles.
 * This repository provides access to news articles from various sources,
 * including the network and local database.
 */
interface NewsRepository {

    /**
     * Searches for news articles based on a query.
     * @param query The search query.
     * @return A [Flow] emitting a list of [CachedArticleEntity] objects representing the search results.
     */
    fun searchNews(query: String): Flow<List<CachedArticleEntity>>

    /**
     * Fetches the top headlines for a specific category and caches them locally.
     * @param category The news category.
     */
    suspend fun fetchTopHeadlines(category: String)

    /**
     * Gets all bookmarked articles as a [Flow].
     * @return A [Flow] emitting a list of [BookmarkedArticleEntity] objects representing the bookmarked articles.
     */
    fun getBookmarkedArticlesFlow(): Flow<List<BookmarkedArticleEntity>>


    /**
     * Gets all cached articles as a [Flow].
     * @return A [Flow] emitting a list of [CachedArticleEntity] objects representing the cached articles.
     */
    fun getCachedArticlesFlow(): Flow<List<CachedArticleEntity>>

    /**
     * Gets a cached article by its ID as a [Flow].
     * @param articleId The ID of the article to retrieve.
     * @return A [Flow] emitting the [CachedArticleEntity] if found, or null if not found.
     */
    fun getCachedArticleByIdFlow(articleId: Int): Flow<CachedArticleEntity?>

    /**
     * Gets a bookmarked article by its ID as a [Flow].
     * @param articleId The ID of the article to retrieve.
     * @return A [Flow] emitting the [BookmarkedArticleEntity] if found, or null if not found.
     */
    fun getBookmarkedArticleByIdFlow(articleId: Int): Flow<BookmarkedArticleEntity?>

    /**
     * Bookmarks an article.
     * @param article The [Article] object to bookmark.
     */
    suspend fun bookmarkArticle(article: Article)

    /**
     * Unbookmarks an article.
     * @param article The [Article] object to unbookmark.
     */
    suspend fun unBookmarkArticle(article: Article)
}

/**
 * Implementation of [NewsRepository] that handles the retrieval, caching, and bookmarking of news articles.
 */
class NewsRepositoryImpl @Inject constructor(
    private val newsNetworkDataSource: NewsNetworkDataSource,
    private val bookmarkedArticlesLocalDataSource: BookmarkedArticlesLocalDataSource,
    private val cachedArticlesLocalDataSource: CachedArticlesLocalDataSource
) : NewsRepository {
    /**
     * Searches for news articles based on the provided query.
     * Fetches data from the network and updates the local cache.
     * @param query The search query.
     * @return A [Flow] emitting a list of cached articles matching the search query.
     */
    override fun searchNews(query: String): Flow<List<CachedArticleEntity>> =
        flow {
            try {
                //        Fetch fresh data from the network and update the local cache
                val response = newsNetworkDataSource.searchNews(query)
                val cachedArticles = response.articles.map {
                    it.toCachedArticleEntity(
                        publishedDate = OffsetDateTime.parse(it.publishedDate)
                    )
                }

                val syncedCachedArticles = syncBookmarkedStatusForCachedArticles(cachedArticles)

                //        Cache them locally
                cachedArticlesLocalDataSource.clearCachedArticles() // clear previous cache
                cachedArticlesLocalDataSource.insertArticles(syncedCachedArticles)

                emitAll(cachedArticlesLocalDataSource.getAllCachedArticlesFlow())
            } catch (e: Exception) {
                throw NoNetworkException(cause = e)
            }
        }.catch { e ->
            if (e is NoNetworkException) {
                throw NoNetworkException(cause = e)
            }
//        Handle errors such as no network
//        for example return cached articles in case of failure
            Log.e("Tag-newsrepository-searchNews", "catch = : ${e.message}")
        }

    /**
     * Fetches top headlines from the network for the given category and caches them locally.
     * @param category The category of news to fetch top headlines for.
     */
    override suspend fun fetchTopHeadlines(category: String) {

        try {
            // Fetch top headlines from the network
            val response = newsNetworkDataSource.getTopHeadlines(category)
            val cachedArticles = response.articles.map {
                it.toCachedArticleEntity(
                    publishedDate = OffsetDateTime.parse(it.publishedDate)
                )
            }

            val syncedCachedArticles = syncBookmarkedStatusForCachedArticles(cachedArticles)

            // Cache them locally by clearing the previous cache and inserting the new articles
            cachedArticlesLocalDataSource.clearCachedArticles()
            cachedArticlesLocalDataSource.insertArticles(syncedCachedArticles)
        } catch (e: Exception) {
            Log.e(
                "Tag-newsrepository-topheadlines-network",
                "catch: ${e.message}"
            )
            throw NoNetworkException(cause = e)
        }
    }

    override fun getBookmarkedArticlesFlow(): Flow<List<BookmarkedArticleEntity>> =
        bookmarkedArticlesLocalDataSource.getAllBookmarkedArticlesFlow()
            .map { it ?: emptyList() } // Emit empty list if null

    override fun getCachedArticlesFlow(): Flow<List<CachedArticleEntity>> =
        cachedArticlesLocalDataSource.getAllCachedArticlesFlow()
            .map { it ?: emptyList() } // Emit empty list if null

    /**
     * Retrieves a bookmarked article by its ID.
     * If the article with the given ID is not found, the first bookmarked article is returned.
     * @param articleId The ID of the article to retrieve.
     * @return A [Flow] emitting the bookmarked article or the first bookmarked article if not found.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCachedArticleByIdFlow(articleId: Int): Flow<CachedArticleEntity?> =
        cachedArticlesLocalDataSource.getCachedArticleByIdFlow(id = articleId)
            .flatMapLatest { article ->
                if (article != null) {
                    flowOf(article)
                } else {
                    cachedArticlesLocalDataSource.getFirstCachedArticleFlow()
                }
            }.catch { exception ->
                // In case of any other error, emit the first article and log the error
                Log.d("Tag-newsrepository-getCachedArticleById", "catch: ${exception.message}")
            }

    /**
     * Retrieves a cached article by its ID.
     * If the article with the given ID is not found, the first cached article is returned.
     * @param articleId The ID of the article to retrieve.
     * @return A [Flow] emitting the cached article or the first cached article if not found.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBookmarkedArticleByIdFlow(articleId: Int): Flow<BookmarkedArticleEntity?> =
        bookmarkedArticlesLocalDataSource.getBookmarkedArticleByIdFlow(id = articleId)
            .flatMapLatest { article ->
                if (article != null) {
                    // If the article with the given ID exists, emit it
                    flowOf(article)
                } else {
                    // If the article with the given ID doesn't exist, emit the first article in the table
                    bookmarkedArticlesLocalDataSource.getFirstBookmarkedArticleFlow()
                }
            }.catch { exception ->
                // In case of any other error, emit the first article and log the error

                Log.d("Tag-newsrepository-getBookmarkedArticleById", "catch: ${exception.message}")
            }

    override suspend fun bookmarkArticle(article: Article) {
        bookmarkedArticlesLocalDataSource.insertBookmarkedArticle(article.toBookmarkedArticleEntity())
        cachedArticlesLocalDataSource.updateArticleBookmarkStatus(article.id, true)
    }

    override suspend fun unBookmarkArticle(article: Article) {
        bookmarkedArticlesLocalDataSource.deleteBookmarkedArticle(article.toBookmarkedArticleEntity())
        cachedArticlesLocalDataSource.updateArticleBookmarkStatus(article.id, false)
    }

    /**
     * Synchronizes the bookmark status of cached articles by comparing them with the list of bookmarked articles.
     * @param cachedArticles The list of cached articles to sync.
     * @return The list of cached articles with updated bookmark status.
     */
    private suspend fun syncBookmarkedStatusForCachedArticles(cachedArticles: List<CachedArticleEntity>): List<CachedArticleEntity> {
        val bookmarkedArticles =
            bookmarkedArticlesLocalDataSource.getAllBookmarkedArticlesFlow().firstOrNull()
        return cachedArticles.map { cachedArticle ->
//            If the newly cached articles title is present in the bookmarked articles, update the isBookmarked flag
            val isBookmarked = bookmarkedArticles?.any { it.title == cachedArticle.title } ?: false
            cachedArticle.copy(isBookmarked = isBookmarked)
        }
    }
}