package com.hekmatullahamin.offnews.domain

import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.data.repository.NewsRepository
import javax.inject.Inject

/**
 * Use case for toggling the bookmark state of an article.
 *
 * This use case updates the bookmark status of an article in the repository.
 * If the article is currently bookmarked, it will be unbookmarked.
 * If the article is not bookmarked, it will be bookmarked.
 *
 * @property newsRepository The repository for accessing news data.
 */
class UpdateBookmarkUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    /**
     * Toggles the bookmark state of an article.
     *
     * @param article The article to update the bookmark state for.
     */
    suspend operator fun invoke(article: Article) {
        if (article.isBookmarked) {
//            unBookmark the article
            newsRepository.unBookmarkArticle(article)
        } else {
//            Bookmark the article
            newsRepository.bookmarkArticle(article)
        }
    }
}