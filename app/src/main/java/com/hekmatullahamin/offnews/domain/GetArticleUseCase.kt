package com.hekmatullahamin.offnews.domain

import android.util.Log
import com.hekmatullahamin.offnews.data.exceptions.EmptyArticleException
import com.hekmatullahamin.offnews.data.local.BookmarkedArticleEntity
import com.hekmatullahamin.offnews.data.local.CachedArticleEntity
import com.hekmatullahamin.offnews.data.local.toUiArticle
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.data.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for retrieving an article by its ID.
 *
 * This use case handles fetching an article either from the cache (for the home tab)
 * or from the bookmarked articles (for other tabs(Bookmark)). It maps the retrieved article
 * to an external representation ([Article]) and emits it as a [Result] within a [Flow].
 *
 * @property newsRepository The repository for accessing news data.
 * @property formatDateUseCase The use case for formatting dates.
 */
class GetArticleUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val formatDateUseCase: FormatDateUseCase
) {
    /**
     * Maps a flow of [CachedArticleEntity] to a flow of [Result] containing [Article].
     *
     * This function handles the transformation of cached articles to their external
     * representation, including formatting the published date. It also handles
     * potential errors during the mapping process and emits them as [Result.failure].
     */
    private fun Flow<CachedArticleEntity?>.mapCachedArticleToExternalArticle(): Flow<Result<Article?>> =
        map { article ->
            Result.success(
                article?.toUiArticle(
                    publishedDate = formatDateUseCase(article.publishedDate)
                )
            )
        }.catch { e ->
            Log.e("Tag-GetArticle-UseCase-CachedMapToExternal", "Error: $e")
            emit(Result.failure(e))
        }

    /**
     * Maps a flow of [BookmarkedArticleEntity] to a flow of [Result] containing [Article].
     *
     * This function handles the transformation of bookmarked articles to their external
     * representation, including formatting the published date. It also handles
     * potential errors during the mapping process and emits them as [Result.failure].
     */
    private fun Flow<BookmarkedArticleEntity?>.mapBookmarkedArticleToExternalArticle(): Flow<Result<Article?>> =
        map { article ->
            Result.success(
                article?.toUiArticle(
                    publishedDate = formatDateUseCase(article.publishedDate)
                )
            )
        }.catch { e ->
            Log.e("Tag-GetArticle-UseCase-BookmarkedMapToExternal", "Error: $e")
            emit(Result.failure(e))
        }


    /**
     * Retrieves an article by its ID, either from the cache or bookmarks.
     *
     * @param articleId The ID of the article to retrieve.
     * @param isHomeTab Indicates whether the article is being retrieved for the home tab.
     *                  If true, the article is fetched from the cache. Otherwise, it's
     *                  fetched from the bookmarked articles.
     * @return A [Flow] emitting a [Result] containing the retrieved [Article] or an error.
     *
     * @throws EmptyArticleException If the article is not found in the bookmarks and `isHomeTab` is false.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(articleId: Int, isHomeTab: Boolean): Flow<Result<Article?>> {
        return if (isHomeTab) {
            newsRepository.getCachedArticleByIdFlow(articleId)
                .mapCachedArticleToExternalArticle()
        } else {
            newsRepository.getBookmarkedArticleByIdFlow(articleId)
                .flatMapLatest { bookmarkedArticle ->
                    if (bookmarkedArticle == null) {
                        Log.d(
                            "Tag-GetArticle-UseCase",
                            "Article with ID $articleId not found in bookmarks"
                        )
                        flow { throw EmptyArticleException() }
                    } else {
                        Log.d(
                            "Tag-GetArticle-UseCase",
                            "Article with ID $articleId found in bookmarks"
                        )
                        flowOf(bookmarkedArticle)
                    }
                }.mapBookmarkedArticleToExternalArticle()
        }
    }

}