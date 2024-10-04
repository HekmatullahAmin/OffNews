package com.hekmatullahamin.offnews.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.domain.FetchTopHeadlinesUseCase
import com.hekmatullahamin.offnews.ui.model.NewsTabType
import com.hekmatullahamin.offnews.ui.uistate.MainUiState
import com.hekmatullahamin.offnews.ui.navigation.HomeDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Main screen.
 *
 * This ViewModel is responsible for managing the overall state of the application,
 * including the current screen, fetching top headlines, and updating the selected
 * article and tab.
 *
 * @property fetchTopHeadlinesUseCase The use case for fetching top headlines.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchTopHeadlinesUseCase: FetchTopHeadlinesUseCase
) : ViewModel() {


    /**
     * The current screen route.
     */
    //    We use this variable to keep track of the current screen that the user is on.
    var currentScreen: String = HomeDestination.route
        private set

    /**
     * Sets the current screen route.
     *
     * @param route The new route.
     */
    fun setCurrentRoute(route: String) {
        currentScreen = route
    }

    /**
     * Indicates if the data is loading.
     */
    var isLoading = mutableStateOf(false)
        private set

    /**
     * The UI state of the Main screen, exposed as a [StateFlow].
     */
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Fetches top headlines for the given category.
     *
     * @param category The news category.
     */
    suspend fun fetchTopHeadlines(category: String) {
        isLoading.value = true
        fetchTopHeadlinesUseCase(category)
            .onSuccess {
                Log.d("Tag-MainViewmodel-fetchTopHeadlines", "Success called")
                isLoading.value = false
            }
            .onFailure {
                Log.d("Tag-MainViewmodel-fetchTopHeadlines", "Failed called")
                isLoading.value = false
            }
    }

    /**
     * Updates the selected article.
     *
     * This function is called when the user selects an article to display its details.
     * In expanded mode, it updates the selected article to be
     * displayed in the details section.
     *
     * @param article The article to select.
     */

    fun updateSelectedArticle(article: Article) {
        _uiState.update {
            it.copy(
                currentArticle = article
            )
        }
    }

    /**
     * Updates the selected tab.
     *
     * @param tabType The tab to select.
     */
    fun updateSelectedTab(tabType: NewsTabType) {
        _uiState.update {
            it.copy(
                currentTab = tabType
            )
        }
    }

    /**
     * Called when the network becomes available.
     * Triggers fetching of top headlines.
     */
    fun onNetworkAvailable() {
        viewModelScope.launch {
            fetchTopHeadlines("general")
        }
    }
}