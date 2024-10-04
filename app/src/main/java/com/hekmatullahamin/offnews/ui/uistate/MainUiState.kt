package com.hekmatullahamin.offnews.ui.uistate

import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.ui.model.NewsTabType

/**
 * Represents the UI state for the main screen of the application.
 *
 * This data class holds information about the currently selected article and the active tab.
 *
 * @property currentArticle The article that is currently selected for viewing, or `null` if no article is selected.
 * @property currentTab The currently active tab in the main screen navigation.
 */
data class MainUiState(
    val currentArticle: Article? = null,
    val currentTab: NewsTabType = NewsTabType.HOME,
)
