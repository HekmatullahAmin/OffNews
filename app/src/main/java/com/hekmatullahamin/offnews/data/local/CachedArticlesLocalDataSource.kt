package com.hekmatullahamin.offnews.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Local data source for managing cached articles.
 */
interface CachedArticlesLocalDataSource {
    /**
     * Inserts a list of cached articles into the local data source.
     * @param articles The list of [CachedArticleEntity] objects to insert.
     */
    suspend fun insertArticles(articles: List<CachedArticleEntity>)

    /**
     * Clears all cached articles from the local data source.
     */
    suspend fun clearCachedArticles()

    /**
     * Updates the bookmark status of a cached article.
     * @param articleId The ID of the article to update.
     * @param isBookmarked The new bookmark status (true for bookmarked, false otherwise).
     */
    suspend fun updateArticleBookmarkStatus(articleId: Int, isBookmarked: Boolean)

    /**
     * Gets all cached articles as a [Flow].
     * @return A [Flow] emitting a [List] of [CachedArticleEntity] objects.
     */
    fun getAllCachedArticlesFlow(): Flow<List<CachedArticleEntity>>

    /**
     * Gets a cached article by its ID as a [Flow].
     * @param id The ID of the article to retrieve.
     * @return A [Flow] emitting the [CachedArticleEntity] if found.
     */
    fun getCachedArticleByIdFlow(id: Int): Flow<CachedArticleEntity?>

    /**
     * Gets the first cached article as a [Flow].
     * @return A [Flow] emitting the first [CachedArticleEntity]
     */
    fun getFirstCachedArticleFlow(): Flow<CachedArticleEntity>
}

/**
 * Implementation of the [CachedArticlesLocalDataSource] interface.
 */
class CachedArticlesLocalDataSourceImpl @Inject constructor(
    private val articleDao: CachedArticleDao
) : CachedArticlesLocalDataSource {
    override suspend fun insertArticles(articles: List<CachedArticleEntity>) =
        articleDao.insertArticles(articles)

    override suspend fun clearCachedArticles() =
        articleDao.clearCachedArticles()

    override suspend fun updateArticleBookmarkStatus(articleId: Int, isBookmarked: Boolean) =
        articleDao.updateBookmarkStatus(articleId = articleId, isBookmarked = isBookmarked)

    override fun getAllCachedArticlesFlow(): Flow<List<CachedArticleEntity>> =
        articleDao.getAllCachedArticlesFlow()

        override fun getCachedArticleByIdFlow(id: Int): Flow<CachedArticleEntity?> =
        articleDao.getCachedArticleByIdFlow(id)

    override fun getFirstCachedArticleFlow(): Flow<CachedArticleEntity> =
        articleDao.getFirstCachedArticleFlow()
}