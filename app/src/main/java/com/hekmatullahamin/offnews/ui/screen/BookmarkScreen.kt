package com.hekmatullahamin.offnews.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.ui.uistate.ScreenState
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme
import com.hekmatullahamin.offnews.ui.viewmodel.BookmarkScreenViewModel

/**
 * Composable function that displays the Bookmark screen.
 *
 * This screen shows the list of bookmarked articles. It handles different
 * screen states: loading, success, and error.
 *
 * @param modifier Modifier used to adjust the layout of the screen.
 * @param lazyListState The state of the LazyColumn used for displaying the articles.
 * @param onArticleActionClicked Callback invoked when an action is performed on an article.
 */
@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    onArticleActionClicked: (Article) -> Unit = {}
) {
    val viewModel: BookmarkScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    BackHandler {
        (context as? Activity)?.finish()
    }

    when (uiState.screenState) {
        is ScreenState.Loading -> BookmarkLoadingScreen(
            modifier = modifier
        )

        is ScreenState.Success -> {
            BookmarkLazyColumnContent(
                modifier = modifier,
                bookmarkedArticles = uiState.bookmarks,
                lazyListState = lazyListState,
                onArticleActionClicked = onArticleActionClicked
            )
        }

        is ScreenState.Error -> {
            val errorState = uiState.screenState as ScreenState.Error
            BookmarkErrorScreen(errorMessage = errorState.errorMessage)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookmarkScreenPreview() {
    OffNewsTheme {
        BookmarkScreen(lazyListState = rememberLazyListState())
    }
}

/**
 * Composable function that displays the content of the Bookmark screen using a LazyColumn.
 *
 * This function is responsible for displaying the list of bookmarked articles
 * within a LazyColumn. It handles the layout and arrangement of the articles.
 *
 * @param modifier Modifier used to adjust the layout of the content.
 * @param bookmarkedArticles The list of bookmarked articles to display.
 * @param lazyListState The state of the LazyColumn.
 * @param onArticleActionClicked Callback invoked when an action is performed on an article.
 */
@Composable
fun BookmarkLazyColumnContent(
    modifier: Modifier = Modifier,
    bookmarkedArticles: List<Article>,
    lazyListState: LazyListState,
    onArticleActionClicked: (Article) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_vertical_space))
    ) {
        items(bookmarkedArticles, key = { news -> news.id }) { bookmarkedArticle ->
            NewsItemCard(
                modifier = Modifier.height(dimensionResource(R.dimen.news_item_card_height)),
                article = bookmarkedArticle,
                onArrowClicked = onArticleActionClicked
            )
        }
    }
}