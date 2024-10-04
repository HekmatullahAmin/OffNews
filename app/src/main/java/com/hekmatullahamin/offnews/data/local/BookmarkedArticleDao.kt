package com.hekmatullahamin.offnews.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for bookmarked articles.
 */
@Dao
interface BookmarkedArticleDao {

    /**
     * Inserts a bookmarked article into the database.
     * If an article with the same ID already exists, it will be replaced.
     * @param article The [BookmarkedArticleEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkedArticle(article: BookmarkedArticleEntity)

    /**
     * Gets a bookmarked article by its ID.
     * @param id The ID of the article to retrieve.
     * @return A [Flow] emitting the [BookmarkedArticleEntity] if found, or null otherwise.
     */
    @Query("SELECT * FROM bookmarked_articles WHERE id =:id")
    fun getBookmarkedArticleByIdFlow(id: Int): Flow<BookmarkedArticleEntity?>

    /**
     * Gets all bookmarked articles.
     * @return A [Flow] emitting a [List] of [BookmarkedArticleEntity] objects.
     */
    @Query("SELECT * FROM bookmarked_articles")
    fun getAllBookmarkedArticlesFlow(): Flow<List<BookmarkedArticleEntity>>

    /**
     * Gets the first bookmarked article.
     * @return A [Flow] emitting the first [BookmarkedArticleEntity]
     */
    @Query("SELECT * FROM bookmarked_articles LIMIT 1")
    fun getFirstBookmarkedArticleFlow(): Flow<BookmarkedArticleEntity>

    /**
     * Deletes a bookmarked article by its title.
     * @param articleTitle The title of the article to delete.
     */
    @Query("DELETE FROM bookmarked_articles WHERE title = :articleTitle")
    suspend fun deleteBookmarkedArticleByTitle(articleTitle: String)
}
