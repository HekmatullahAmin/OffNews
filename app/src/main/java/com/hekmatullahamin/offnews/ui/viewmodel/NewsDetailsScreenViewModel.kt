package com.hekmatullahamin.offnews.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.domain.GetArticleUseCase
import com.hekmatullahamin.offnews.domain.UpdateBookmarkUseCase
import com.hekmatullahamin.offnews.ui.uistate.DetailsUiState
import com.hekmatullahamin.offnews.ui.uistate.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

/**
 * ViewModel for the News Details screen.
 *
 * This ViewModel is responsible for fetching and managing the state of a news article,
 * including handling bookmark operations. It exposes the UI state as a [StateFlow] of
 * [DetailsUiState], which can be observed by the composable UI to display the article
 * details and handle loading/error states.
 *
 * @property getArticleUseCase The use case for retrieving a news article.
 * @property updateBookmarkUseCase The use case for updating the bookmark status of an article.
 */
@HiltViewModel
class NewsDetailsScreenViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val updateBookmarkUseCase: UpdateBookmarkUseCase
) : ViewModel() {

    /**
     * The UI state of the News Details screen, exposed as a [StateFlow].
     *
     * This state flow emits updates whenever the article details change or the
     * loading/error state changes.
     */
    private val _uiState: MutableStateFlow<DetailsUiState> = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailsUiState()
    )

    /**
     * Fetches the news article with the given ID and updates the UI state.
     *
     * @param articleId The ID of the article to fetch.
     * @param isHomeTab Indicates whether the article is being accessed from the Home tab.
     */
    fun getArticle(articleId: Int, isHomeTab: Boolean) {
        viewModelScope.launch {
            getArticleUseCase(articleId, isHomeTab)
                .distinctUntilChanged()
                .collect { result ->
                    result.onSuccess { article ->
                        Log.d("Tag-DetailsViemodel", "onSuccess: ${article}")
                        _uiState.update { currentState ->
                            currentState.copy(
                                article = article,
                                screenState = ScreenState.Success
                            )
                        }
                    }
                    result.onFailure { exception ->
                        _uiState.update { currentState ->
                            Log.d(
                                "Tag-DetailsViemodel",
                                "failed to get Article: ${exception.message}"
                            )
                            currentState.copy(
                                screenState = ScreenState.Error(
                                    exception.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                }
        }
    }

    /**
     * Toggles the bookmark status of the given article.
     *
     * @param article The article to toggle the bookmark status for.
     */
    fun toggleBookmark(article: Article) {
        viewModelScope.launch {
            updateBookmarkUseCase(article)
        }
    }
}