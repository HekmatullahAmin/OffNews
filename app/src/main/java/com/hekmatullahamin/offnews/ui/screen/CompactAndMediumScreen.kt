package com.hekmatullahamin.offnews.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.ui.navigation.NavigationItemContent
import com.hekmatullahamin.offnews.ui.uistate.MainUiState
import com.hekmatullahamin.offnews.ui.OffNewsAppState
import com.hekmatullahamin.offnews.ui.navigation.NewsContentNavHost
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme
import com.hekmatullahamin.offnews.utils.NavigationConstants
import androidx.compose.material3.NavigationRail
import androidx.compose.ui.res.stringResource
import com.hekmatullahamin.offnews.ui.model.NewsTabType

/**
 * Composable function that represents the UI for compact and medium screens.
 *
 * This function displays the navigation rail (if applicable) and the news content
 * using the [NewsContentNavHost]. It handles the layout for these screen sizes.
 *
 * @param modifier Modifier used to adjust the layout of the screen.
 * @param currentTab The currently selected [NewsTabType].
 * @param currentRoute The current route to be used as the start destination.
 * @param onTabPressed Callback invoked when a tab is pressed in the navigation rail.
 * @param onFeaturedNewsCardClicked Callback invoked when a featured news card is clicked.
 * @param onArticleClicked Callback invoked when an article is clicked.
 * @param homeLazyListState The [LazyListState] for the Home screen's lazy list.
 * @param bookmarkLazyListState The [LazyListState] for the Bookmark screen's lazy list.
 * @param offNewsAppState The app's state, providing access to navigation and other properties.
 */
@Composable
fun CompactAndMediumScreen(
    modifier: Modifier = Modifier,
    currentTab: NewsTabType,
    currentRoute: String,
    onTabPressed: (NewsTabType) -> Unit = {},
    onFeaturedNewsCardClicked: (Article) -> Unit = {},
    onArticleClicked: (Article) -> Unit = {},
    homeLazyListState: LazyListState,
    bookmarkLazyListState: LazyListState,
    offNewsAppState: OffNewsAppState
) {
    Scaffold { contentPadding ->
        Row(modifier = modifier.padding(contentPadding)) {
            if (offNewsAppState.shouldShowNavRail) {
                NavigationRail(
                    currentTab = currentTab,
                    onTabPressed = onTabPressed
                )
            }
            NewsContentNavHost(
                navController = offNewsAppState.navController,
                currentTabType = currentTab,
                onFeaturedNewsCardClicked = onFeaturedNewsCardClicked,
                onArticleClicked = onArticleClicked,
                homeLazyListState = homeLazyListState,
                bookmarkLazyListState = bookmarkLazyListState,
                currentRoute = currentRoute
            )
        }
    }
}

/**
 * Composable function that represents the bottom navigation bar.
 *
 * This function displays a bottom navigation bar with icons and text for
 * navigating between different sections of the app.
 *
 * @param modifier Modifier used to adjust the layout of the bottom navigation bar.
 * @param currentTab The currently selected tab.
 * @param onTabPressed Callback invoked when a tab is pressed.
 * @param navigationItemContentList The list of navigation items to display.
 */
@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    currentTab: NewsTabType = NewsTabType.HOME,
    onTabPressed: ((NewsTabType) -> Unit) = {},
    navigationItemContentList: List<NavigationItemContent> = NavigationConstants.NavigationItems
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.newsTabType,
                onClick = { onTabPressed(navItem.newsTabType) },
                icon = {
                    Icon(
                        painter = painterResource(navItem.iconResId),
                        contentDescription = stringResource(id = navItem.textResId)
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    OffNewsTheme {
        BottomNavigationBar()
    }
}

/**
 * Composable function that represents the navigation rail.
 *
 * This function displays a navigation rail with icons and text for
 * navigating between different sections of the app.
 *
 * @param modifier Modifier used to adjust the layout of the navigation rail.
 * @param currentTab The currently selected tab.
 * @param onTabPressed Callback invoked when a tab is pressed.
 * @param navigationItems The list of navigation items to display.
 */
@Composable
fun NavigationRail(
    modifier: Modifier = Modifier,
    currentTab: NewsTabType = NewsTabType.HOME,
    onTabPressed: ((NewsTabType) -> Unit) = {},
    navigationItems: List<NavigationItemContent> = NavigationConstants.NavigationItems
) {
    NavigationRail(
        modifier = modifier
    ) {
        for (item in navigationItems) {
            NavigationRailItem(
                selected = currentTab == item.newsTabType,
                onClick = { onTabPressed(item.newsTabType) },
                icon = {
                    Icon(
                        painter = painterResource(item.iconResId),
                        contentDescription = stringResource(id = item.textResId)
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationRailPreview() {
    OffNewsTheme {
        NavigationRail()
    }
}