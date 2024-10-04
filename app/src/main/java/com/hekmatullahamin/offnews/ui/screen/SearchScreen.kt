package com.hekmatullahamin.offnews.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.ui.composables.NewsIconButton
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme
import com.hekmatullahamin.offnews.ui.viewmodel.SearchViewModel

/**
 * Composable function that displays the Search screen.
 *
 * This screen allows users to search for news articles using a search query.
 * It displays a search bar and a list of recent searches.
 *
 * @param modifier Modifier used to adjust the layout of the screen.
 * @param onSearchComplete Callback invoked when the search is completed and the screen should navigate back to HomeScreen.
 */
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onSearchComplete: () -> Unit = {},
) {
    // Get the keyboard controller to control the software keyboard
    val keyboardController = LocalSoftwareKeyboardController.current
    val viewModel: SearchViewModel = hiltViewModel()

//    Observe the UI state to handle search completion and navigation
//    In this implementation, navigation and state management are handled separately to maintain a clean architecture.
//    The ViewModel manages only the business logic, such as fetching news data, while the UI is responsible for navigation.
//    This separation ensures that navigation, which is a UI concern, remains flexible and easily adjustable without affecting the core logic.
//    The UI observes changes in the ViewModel’s state, and once the search operation completes, it triggers navigation back to the appropriate screen.
//    This approach keeps the codebase modular, testable, and adaptable to different UI configurations.

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(viewModel.uiState.value.isSearchCompleted, lifecycle) {
        if (viewModel.uiState.value.isSearchCompleted) {
            Log.d(
                "Tag-SearchScreen",
                "LaunchedEffect called isComplete ${viewModel.uiState.value.isSearchCompleted}"
            )
//            Reset search state before navigation back to avoid repeated navigation
            viewModel.resetSearchState()
            onSearchComplete() // Trigger navigation back to the previous screen
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_vertical_space))
    ) {
        OutlinedTextField(
            value = viewModel.uiState.value.searchQuery,
            onValueChange = {
                viewModel.updateSearchQuery(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
            trailingIcon = {
                NewsIconButton(
                    contentDescription = stringResource(R.string.clear_search_query_content_description),
                    icon = R.drawable.baseline_clear_24,
                    onClick = { viewModel.updateSearchQuery("") },
                    modifier = Modifier.size(dimensionResource(R.dimen.clear_search_icon_size)),
                    iconInnerPadding = R.dimen.clear_icon_button_inner_padding
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search // Set keyboard action to search
            ),
            keyboardActions = KeyboardActions(
//                Trigger the onSearch callback with the current search query
                onSearch = {
//                    by default it will search the searchQuery in viewmodel
                    viewModel.searchNews()
//                    Optionally, hide the keyboard after the search action
                    keyboardController?.hide()
                }
            )
        )
        Text(
            text = stringResource(R.string.recent_search_label),
            style = MaterialTheme.typography.titleMedium
        )
//        TODO: save searched keywords in Preferences DataStore and load it back from there
        RecentSearchNewsList(
            recentSearches = listOf(
                "Technology",
                "Business",
                "Health"
            ),
            onRecentSearchClicked = { query ->
//                trigger the search for this recent query
                viewModel.searchNews(query)
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    OffNewsTheme {
        SearchScreen()
    }
}

/**
 * Composable function that displays a list of recent search queries.
 *
 * This list allows users to quickly select a previous search query.
 *
 * @param modifier Modifier used to adjust the layout of the list.
 * @param onRecentSearchClicked Callback invoked when a recent search query is clicked.
 * @param recentSearches The list of recent search queries to display.
 */
@Composable
fun RecentSearchNewsList(
    modifier: Modifier = Modifier,
    onRecentSearchClicked: (String) -> Unit = {},
    recentSearches: List<String>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_vertical_space))
    ) {
        items(recentSearches) { search ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = search,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = modifier
                        .weight(1F)
                        .clickable { onRecentSearchClicked(search) }
                )
                NewsIconButton(
                    contentDescription = stringResource(R.string.remove_recent_search_content_description),
                    icon = R.drawable.baseline_clear_24,
                    onClick = { },
                    modifier = Modifier.size(dimensionResource(R.dimen.remove_searched_news_icon_size)),
                    iconInnerPadding = R.dimen.remove_searched_news_icon_button_inner_padding
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecentSearchNewsListPreview() {
    OffNewsTheme {
        RecentSearchNewsList(
            recentSearches = listOf(
                "Technology",
                "Business",
                "Health"
            )
        )
    }
}