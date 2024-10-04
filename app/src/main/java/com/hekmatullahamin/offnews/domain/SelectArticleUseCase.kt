package com.hekmatullahamin.offnews.domain

import com.hekmatullahamin.offnews.data.model.Article
import javax.inject.Inject

/**
 * Use case for selecting the article to display.
 *
 * This use case selects the appropriate article to display based on the
 * currently selected article and the new list of articles. It prioritizes
 * the currently selected article if it exists in the new list. Otherwise,
 * it selects the first article from the list. If the list is empty, it
 * returns a default `Article` object.
 */
//TODO: the name shoug get changed and also the description when it is used
class SelectArticleUseCase @Inject constructor() {

    /**
     * Selects the article to display.
     *
     * @param currentSelectedArticle The currently selected article (if any).
     * @param articles The new list of articles.
     * @return The selected article to display.
     */
    operator fun invoke(
        currentSelectedArticle: Article?,
        articles: List<Article>
    ): Article {
        return articles.find { article ->
            article.title == currentSelectedArticle?.title
        } ?: articles.firstOrNull() ?: Article()
    }
}