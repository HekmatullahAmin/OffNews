package com.hekmatullahamin.offnews.ui.uistate

/**
 * Represents the UI state for the search functionality.
 *
 * This data class holds information about the current state of the search,
 * including whether a search is in progress and the current search query.
 *
 * @property isSearchCompleted Indicates whether a search is currently in progress.
 * @property searchQuery The current search query entered by the user.
 */
data class SearchUiState(
    val isSearchCompleted: Boolean = false,
    val searchQuery: String = "",
)
