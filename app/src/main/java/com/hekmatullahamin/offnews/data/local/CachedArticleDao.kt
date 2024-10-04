package com.hekmatullahamin.offnews.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for cached articles.
 */
@Dao
interface CachedArticleDao {

    /**
     * Inserts a list of cached articles into the database.
     * If an article with the same ID already exists, it will be replaced.
     * @param articles The list of [CachedArticleEntity] objects to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<CachedArticleEntity>)

    /**
     * Gets all cached articles as a [Flow].
     * @return A [Flow] emitting a [List] of [CachedArticleEntity] objects.
     */
    @Query("SELECT * FROM cached_articles")
    fun getAllCachedArticlesFlow(): Flow<List<CachedArticleEntity>>

    /**
     * Gets a cached article by its ID as a [Flow].
     * @param id The ID of the article to retrieve.
     * @return A [Flow] emitting the [CachedArticleEntity] if found.
     */
    @Query("SELECT * FROM cached_articles WHERE id =:id")
    fun getCachedArticleByIdFlow(id: Int): Flow<CachedArticleEntity?>

    /**
     * Clears all cached articles from the database.
     */
    @Query("DELETE FROM cached_articles")
    suspend fun clearCachedArticles()

    /**
     * Updates the bookmark status of a cached article.
     * @param articleId The ID of the article to update.
     * @param isBookmarked The new bookmark status (true for bookmarked, false otherwise).
     */
    @Query("UPDATE cached_articles SET is_bookmarked = :isBookmarked WHERE id = :articleId")
    suspend fun updateBookmarkStatus(articleId: Int, isBookmarked: Boolean)

    /**
     * Gets the first cached article as a [Flow].
     * @return A [Flow] emitting the first [CachedArticleEntity].
     */
    @Query("SELECT * FROM cached_articles LIMIT 1")
    fun getFirstCachedArticleFlow(): Flow<CachedArticleEntity>
}
