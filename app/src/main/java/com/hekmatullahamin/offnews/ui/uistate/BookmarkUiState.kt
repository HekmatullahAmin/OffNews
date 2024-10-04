package com.hekmatullahamin.offnews.ui.uistate

import com.hekmatullahamin.offnews.data.model.Article

/**
 * Represents the UI state for the Bookmark screen.
 *
 * This data class holds information about the current state of the Bookmark screen,
 * including the list of bookmarked articles and the screen's loading state.
 *
 * @property bookmarks The list of articles that have been bookmarked by the user.
 * @property screenState The current state of the screen, such as loading, success, or error.
 */
data class BookmarkUiState(
    val bookmarks: List<Article> = emptyList(),
    val screenState: ScreenState = ScreenState.Loading,
)
