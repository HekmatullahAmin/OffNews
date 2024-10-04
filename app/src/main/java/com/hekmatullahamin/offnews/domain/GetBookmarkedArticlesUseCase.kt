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
 * Use case for retrieving all bookmarked articles.
 *
 * This use case fetches the list of bookmarked articles from the repository,
 * maps them to a displayable format ([Article]), and emits them as a [Result]
 * within a [Flow].
 *
 * @property newsRepository The repository for accessing news data.
 * @property formatDateUseCase The use case for formatting dates.
 */
class GetBookmarkedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val formatDateUseCase: FormatDateUseCase
) {

    /**
     * Retrieves all bookmarked articles.
     *
     * @return A [Flow] emitting a [Result] containing the list of bookmarked
     *         [Article]s or an error. If there are no bookmarked articles,
     *         a [Result.failure] with an [EmptyArticleException] is emitted.
     */
    operator fun invoke(): Flow<Result<List<Article>>> {
        return newsRepository.getBookmarkedArticlesFlow()
            .map { bookmarkedArticles ->
                val formatedArticles = bookmarkedArticles.map { article ->
                    article.toUiArticle(publishedDate = formatDateUseCase(article.publishedDate))
                }

                if (formatedArticles.isEmpty()) {
                    Result.failure(EmptyArticleException("No bookmarked articles available.")) //Emit failure
                } else {
                    Result.success(formatedArticles)
                }
            }.catch {
                Log.d("Tag-GetBookmarkedArticle-UseCase", "Error : ${it.message}")
                emit(Result.failure(it))
            }
    }
}