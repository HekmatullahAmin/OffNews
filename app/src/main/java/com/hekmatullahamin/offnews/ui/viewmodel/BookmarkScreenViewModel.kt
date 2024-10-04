package com.hekmatullahamin.offnews.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.offnews.domain.GetBookmarkedArticlesUseCase
import com.hekmatullahamin.offnews.ui.uistate.BookmarkUiState
import com.hekmatullahamin.offnews.ui.uistate.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Bookmark screen.
 *
 * This ViewModel is responsible for fetching and managing the list of bookmarked articles.
 * It exposes the UI state as a [StateFlow] of [BookmarkUiState], which can be observed by
 * the composable UI to display the bookmarks and handle loading/error states.
 *
 * @property getBookmarkedArticlesUseCase The use case for retrieving bookmarked articles.
 */
@HiltViewModel
class BookmarkScreenViewModel @Inject constructor(
    private val getBookmarkedArticlesUseCase: GetBookmarkedArticlesUseCase
) : ViewModel() {

    /**
     * The UI state of the Bookmark screen, exposed as a [StateFlow].
     *
     * This state flow emits updates whenever the list of bookmarks changes or
     * when the loading/error state changes.
     */
    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState> = _uiState
        .onStart {
            loadBookmarks()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500),
            initialValue = BookmarkUiState()
        )


    /**
     * Loads the bookmarked articles and updates the UI state.
     *
     * This function is called when the ViewModel is initialized and whenever
     * the user requests a refresh of the bookmarks.
     */
    private fun loadBookmarks() {
        Log.d("Tag-getBookmark_Viewmodel", "getBookmarkedArticles called")
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    screenState = ScreenState.Loading
                )
            }
            getBookmarkedArticlesUseCase()
                .distinctUntilChanged()
                .collect { result ->
                    result.onSuccess { articles ->
                        Log.d(
                            "Tag-getBookmark_Viewmodel",
                            " Success: firstArt = ${articles[0]} + size = ${articles.size}"
                        )
                        _uiState.update { currentState ->
                            currentState.copy(
                                bookmarks = articles,
                                screenState = ScreenState.Success,
                            )
                        }
                    }
                    result.onFailure { throwable ->
                        _uiState.update { currentState ->
                            Log.d("Tag-getBookmark_Viewmodel_Failure", "Error : ${throwable.message}")
                            currentState.copy(
                                screenState = ScreenState.Error(
                                    throwable.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                }
        }
    }
}