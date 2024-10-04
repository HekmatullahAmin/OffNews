package com.hekmatullahamin.offnews.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.offnews.data.exceptions.EmptyArticleException
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.domain.GetCachedArticlesUseCase
import com.hekmatullahamin.offnews.domain.SelectArticleUseCase
import com.hekmatullahamin.offnews.ui.uistate.HomeUiState
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
 * ViewModel for the Home screen.
 *
 * This ViewModel is responsible for fetching and managing the list of cached articles
 * and selecting the appropriate article to display in the **featured card**.
 * It exposes the UI state as a [StateFlow] of [HomeUiState], which can be observed by
 * the composable UI to display the articles and handle loading/error states.
 *
 * @property selectArticleUseCase The use case for selecting the article for the featured card.
 * @property getCachedArticlesUseCase The use case for retrieving cached articles.
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val selectArticleUseCase: SelectArticleUseCase,
    private val getCachedArticlesUseCase: GetCachedArticlesUseCase
) : ViewModel() {


    /**
     * The UI state of the Home screen, exposed as a [StateFlow].
     *
     * This state flow emits updates whenever the list of articles changes, the selected
     * article changes, or the loading/error state changes. It starts with an initial
     * state of [ScreenState.Loading] to indicate that data is being fetched.
     */
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState
        .onStart {
            loadCachedArticles("general")
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = HomeUiState()
        )

    /**
     * Loads the cached articles and updates the UI state.
     *
     * @param category The category of articles to load (currently not used).
     */
    private fun loadCachedArticles(category: String) {
        Log.d("Tag-getCachedArticles-Viewmodel", "getCachedArticles called")
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    screenState = ScreenState.Loading
                )
            }
            getCachedArticlesUseCase()
                .distinctUntilChanged()
                .collect { result ->
                    result.onSuccess { articles ->
                        Log.d(
                            "Tag-getCachedArticles_Viewmodel",
                            " Success: firstArt = ${articles[0]} + size = ${articles.size}"
                        )
//                        Handle success case, update the UI with articles
                        //                    Get the currently selected article from the state
                        val currentSelectedArticle =
                            _uiState.value.currentSelectedArticle
                        // If an article is already selected for this tab, retain it, otherwise select the first article
                        val updatedSelectedArticle = selectArticleUseCase(
                            currentSelectedArticle = currentSelectedArticle,
                            // it is formatted cause the getCachedArticleUseCase return formatted Articles
                            articles = articles
                        )

                        _uiState.update { currentState ->
                            currentState.copy(
                                articles = articles,
                                screenState = ScreenState.Success,
                                currentSelectedArticle = updatedSelectedArticle
                            )
                        }
                    }.onFailure { throwable ->
                        Log.d("Tag-getCachedArticles_Viewmodel_Failure", "Error : ${throwable.message}")
                        if (throwable is EmptyArticleException) {
                            _uiState.update { currentState ->
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

    /**
     * Sets the currently selected article.
     *
     * This function is called when the user selects an article from the list.
     *
     * @param article The article to select.
     */
    fun setCurrentlySelectedArticle(article: Article) {
        _uiState.update { currentState ->
            currentState.copy(
                currentSelectedArticle = article
            )
        }
    }
}