package com.hekmatullahamin.offnews.data.repository

import com.hekmatullahamin.offnews.data.exceptions.NoNetworkException
import com.hekmatullahamin.offnews.data.local.BookmarkedArticleEntity
import com.hekmatullahamin.offnews.data.local.CachedArticleEntity
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.data.model.toBookmarkedArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeNewsRepository /*@Inject constructor*/(
    private val networkArticles: MutableList<CachedArticleEntity> = mutableListOf(),
    private val cachedArticles: MutableList<CachedArticleEntity> = mutableListOf(),
    private val bookmarkedArticles: MutableList<BookmarkedArticleEntity> = mutableListOf()
) : NewsRepository {

    //    Control flags to simulate different error scenarios
    var shouldThrowNetworkError = false

    override fun searchNews(query: String): Flow<List<CachedArticleEntity>> = flow {
//        Simulate a network error
        if (shouldThrowNetworkError) {
            throw NoNetworkException()
        }

//        Return filtered articles if the query matches, otherwise empty list
        val cachedArticlesList =
            networkArticles.filter { it.title.contains(query, ignoreCase = true) }

        val syncedCachedArticles = syncBookmarkedStatusForCachedArticles(cachedArticlesList)
        cachedArticles.clear()
        cachedArticles.addAll(syncedCachedArticles)
        emit(cachedArticles)
    }.catch { e ->
        if (e is NoNetworkException) {
            throw NoNetworkException(cause = e)
        }
    }

    override suspend fun fetchTopHeadlines(category: String) {
        if (shouldThrowNetworkError) {
            throw NoNetworkException()
        }

        val cachedArticleList =
            networkArticles.filter { it.title.contains(category, ignoreCase = true) }

        val syncedCachedArticles = syncBookmarkedStatusForCachedArticles(cachedArticleList)
        cachedArticles.clear()
        cachedArticles.addAll(syncedCachedArticles)
    }

    override fun getBookmarkedArticlesFlow(): Flow<List<BookmarkedArticleEntity>> = flow {
        emit(bookmarkedArticles)
    }.catch {
        emit(mutableListOf())
    }

    override fun getCachedArticlesFlow(): Flow<List<CachedArticleEntity>> = flow {
        emit(cachedArticles)
    }.catch {
        emit(mutableListOf())
    }

    override fun getCachedArticleByIdFlow(articleId: Int): Flow<CachedArticleEntity?> = flow {
        val article = cachedArticles.find { it.id == articleId }
        if (article != null) {
            emit(article)
        } else {
            emit(cachedArticles.first())
        }
    }.catch {

    }

    override fun getBookmarkedArticleByIdFlow(articleId: Int): Flow<BookmarkedArticleEntity?> =
        flow {
            val article = bookmarkedArticles.find { it.id == articleId }
            if (article != null) {
                emit(article)
            } else {
                emit(bookmarkedArticles.first())
            }
        }.catch {

        }

    override suspend fun bookmarkArticle(article: Article) {
        bookmarkedArticles.add(article.toBookmarkedArticleEntity())
//        find the corresponding cached article and update its bookmark status
        val cachedArticleIndex = cachedArticles.indexOfFirst { it.id == article.id }
        cachedArticles[cachedArticleIndex] = cachedArticles[cachedArticleIndex].copy(
            isBookmarked = true
        )
    }

    override suspend fun unBookmarkArticle(article: Article) {
        bookmarkedArticles.removeIf { it.title == article.title }
        //        find the corresponding cached article and update its bookmark status
        val cachedArticleIndex = cachedArticles.indexOfFirst { it.id == article.id }
        cachedArticles[cachedArticleIndex] = cachedArticles[cachedArticleIndex].copy(
            isBookmarked = false
        )
    }

    /**
     * Synchronizes the bookmark status of cached articles by comparing them with the list of bookmarked articles.
     * @param cachedArticles The list of cached articles to sync.
     * @return The list of cached articles with updated bookmark status.
     */
    private fun syncBookmarkedStatusForCachedArticles(cachedArticles: List<CachedArticleEntity>): List<CachedArticleEntity> {
        return cachedArticles.map { cachedArticle ->
            val isBookmarked = bookmarkedArticles.any { it.title == cachedArticle.title }
            cachedArticle.copy(isBookmarked = isBookmarked)
        }
    }
}