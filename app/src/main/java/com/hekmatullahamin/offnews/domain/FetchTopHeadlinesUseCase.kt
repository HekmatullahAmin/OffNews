package com.hekmatullahamin.offnews.domain

import android.util.Log
import com.hekmatullahamin.offnews.data.exceptions.EmptyArticleException
import com.hekmatullahamin.offnews.data.exceptions.NoNetworkException
import com.hekmatullahamin.offnews.data.local.toUiArticle
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for fetching and storing top headlines for a given category.
 * This use case fetches top headlines from the news repository and stores them in the database.
 * It returns a [Result] indicating the success or failure of the operation.
 *
 * @param category The category of news to fetch.
 * @return A [Result] indicating the success or failure of the operation.
 *         [Result.success] is returned if the headlines were fetched and stored successfully.
 *         [Result.failure] is returned if there was an error, such as a network error.
 */
class FetchTopHeadlinesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(category: String): Result<Unit> {
        return try {
            newsRepository.fetchTopHeadlines(
                category = category
            )
            Result.success(Unit)
        } catch (e: NoNetworkException) {
            Log.d("Tag-fetchtopHeadline-UseCase", "catch: ${e.message}")
            Result.failure(e)
        }
    }
}