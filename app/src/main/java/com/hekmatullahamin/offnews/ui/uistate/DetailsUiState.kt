package com.hekmatullahamin.offnews.ui.uistate

import com.hekmatullahamin.offnews.data.model.Article

/**
 * Represents the UI state for the Details screen.
 *
 * This data class holds information about the current state of the Details screen,
 * including the article being displayed and the screen's loading state.
 *
 * @property article The article to display, or `null` if the article is not yet loaded.
 * @property screenState The current state of the screen, which can be Loading, Success, or Error.
 * */
data class DetailsUiState(
    val article: Article? = null,
    val screenState: ScreenState = ScreenState.Loading
)
