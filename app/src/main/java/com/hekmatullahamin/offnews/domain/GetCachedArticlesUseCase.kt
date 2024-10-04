package com.hekmatullahamin.offnews.domain

import android.util.Log
import com.hekmatullahamin.offnews.data.exceptions.EmptyArticleException
import com.hekmatullahamin.offnews.data.local.toUiArticle
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for retrieving all cached articles.
 *
 * This use case fetches the list of cached articles from the repository,
 * maps them to a displayable format ([Article]), and emits them as a [Result]
 * within a [Flow].
 *
 * @property newsRepository The repository for accessing news data.
 * @property formatDateUseCase The use case for formatting dates.
 */
class GetCachedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val formatDateUseCase: FormatDateUseCase
) {

    /**
     * Retrieves all cached articles.
     *
     * @return A [Flow] emitting a [Result] containing the list of cached
     *         [Article]s or an error. If there are no cached articles,
     *         a [Result.failure] with an [EmptyArticleException] is emitted.
     */
    operator fun invoke(): Flow<Result<List<Article>>> {
        return newsRepository.getCachedArticlesFlow()
            .map { articles ->
                val formattedArticles = articles.map { article ->
                    article.toUiArticle(publishedDate = formatDateUseCase(article.publishedDate))
                }
                if (formattedArticles.isEmpty()) {
                    Result.failure(EmptyArticleException("No news articles available."))
                } else {
                    Log.d("Tag-getCachedArticles-UseCase", "Success = first1 = : ${formattedArticles[0]}")
                    Result.success(formattedArticles)
                }
            }.catch {
                Log.d("Tag-getCachedArtice-UseCase", "Error getting cachedArticles: ${it.message}")
                emit(Result.failure(it))
            }
    }
}