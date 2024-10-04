package com.hekmatullahamin.offnews.ui.uistate

import com.hekmatullahamin.offnews.data.model.Article

//TODO: Consider using a more neutral state like ScreenState.Idle or ScreenState.Content
// as the initial state. This allows you to handle different scenarios more flexibly.
/**
 * Represents the UI state for the Home screen.
 *
 * This data class holds information about the current state of the Home screen,
 * including the list of articles to display, the currently selected article,
 * and the screen's loading state.
 *
 * @property articles The list of articles to display on the Home screen.
 * @property currentSelectedArticle The article that is currently selected for viewing in detail, or `null` if no article is selected.
 * @property screenState The current state of the screen, which can be Loading, Success, or Error.
 */
data class HomeUiState(
    val articles: List<Article> = emptyList(),
    val currentSelectedArticle: Article? = null,
    val screenState: ScreenState = ScreenState.Loading
)
