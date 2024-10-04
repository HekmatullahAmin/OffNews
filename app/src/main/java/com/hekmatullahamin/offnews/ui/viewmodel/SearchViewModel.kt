package com.hekmatullahamin.offnews.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.offnews.data.repository.NewsRepository
import com.hekmatullahamin.offnews.ui.uistate.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Search screen.
 *
 * This ViewModel is responsible for managing the state and logic of the search functionality.
 * It handles updating the search query, fetching search results from the repository,
 * and resetting the search state.
 *
 * @property newsRepository The repository for accessing news data.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    /**
     * The UI state of the Search screen.
     */
    var uiState = mutableStateOf(SearchUiState())
        private set

    /**
     * Updates the search query in the UI state.
     *
     * @param query The new search query.
     */
    fun updateSearchQuery(query: String) {
        uiState.value = uiState.value.copy(searchQuery = query)
    }

    /**
     * Fetches news articles for the given search query.
     *
     * @param query The search query (defaults to the current query in the UI state).
     */
    fun searchNews(query: String = uiState.value.searchQuery) {
        viewModelScope.launch {
            newsRepository.searchNews(query)
                .catch { e ->
                    Log.d("Tag-Search_viewmodel", "Error fetching news: ${e.message}")
                    // Handle errors if necessary
                }
                .collect { articles ->
                    Log.d("Tag-Search_viewmodel", "fetched news: ${articles?.size}")
                    // The data will be cached
//                    After fetching, update the UI state to indicate search is complete
                    uiState.value = uiState.value.copy(isSearchCompleted = true)
                }
        }
    }

    /**
     * Resets the search state to its initial value.
     */
    fun resetSearchState() {
        uiState.value = uiState.value.copy(
            isSearchCompleted = false,
            searchQuery = ""
        )
    }
}