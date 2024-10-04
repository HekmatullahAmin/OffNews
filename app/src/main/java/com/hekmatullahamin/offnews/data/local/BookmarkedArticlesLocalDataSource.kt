package com.hekmatullahamin.offnews.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Local data source for managing bookmarked articles.
 */
interface BookmarkedArticlesLocalDataSource {
    /**
     * Inserts a bookmarked article into the local data source.
     * @param article The [BookmarkedArticleEntity] to insert.
     */
    suspend fun insertBookmarkedArticle(article: BookmarkedArticleEntity)
    /**
     * Deletes a bookmarked article from the local data source.
     * @param article The [BookmarkedArticleEntity] to delete.
     */
    suspend fun deleteBookmarkedArticle(article: BookmarkedArticleEntity)
    /**
     * Gets all bookmarked articles as a [Flow].
     * @return A [Flow] emitting a [List] of [BookmarkedArticleEntity] objects.
     */
    fun getAllBookmarkedArticlesFlow(): Flow<List<BookmarkedArticleEntity>>
    /**
     * Gets a bookmarked article by its ID as a [Flow].
     * @param id The ID of the article to retrieve.
     * @return A [Flow] emitting the [BookmarkedArticleEntity] if found, or null otherwise.
     */
    fun getBookmarkedArticleByIdFlow(id: Int): Flow<BookmarkedArticleEntity?>
    /**
     * Gets the first bookmarked article as a [Flow].
     * @return A [Flow] emitting the first [BookmarkedArticleEntity].
     */
    fun getFirstBookmarkedArticleFlow(): Flow<BookmarkedArticleEntity>
}

/**
 * Implementation of the [BookmarkedArticlesLocalDataSource] interface.
 */
class BookmarkedArticlesLocalDataSourceImpl @Inject constructor(
    private val bookmarkedArticleDao: BookmarkedArticleDao

) : BookmarkedArticlesLocalDataSource {
    override suspend fun insertBookmarkedArticle(article: BookmarkedArticleEntity) =
        bookmarkedArticleDao.insertBookmarkedArticle(article)

    override suspend fun deleteBookmarkedArticle(article: BookmarkedArticleEntity) =
        bookmarkedArticleDao.deleteBookmarkedArticleByTitle(article.title)

    override fun getAllBookmarkedArticlesFlow(): Flow<List<BookmarkedArticleEntity>> =
        bookmarkedArticleDao.getAllBookmarkedArticlesFlow()

    override fun getBookmarkedArticleByIdFlow(id: Int): Flow<BookmarkedArticleEntity?> =
        bookmarkedArticleDao.getBookmarkedArticleByIdFlow(id)

    override fun getFirstBookmarkedArticleFlow(): Flow<BookmarkedArticleEntity> =
        bookmarkedArticleDao.getFirstBookmarkedArticleFlow()
}