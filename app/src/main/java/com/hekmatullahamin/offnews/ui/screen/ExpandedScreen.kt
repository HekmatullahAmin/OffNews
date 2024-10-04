package com.hekmatullahamin.offnews.ui.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.ui.model.NewsTabType
import com.hekmatullahamin.offnews.ui.navigation.NavigationItemContent
import com.hekmatullahamin.offnews.ui.uistate.MainUiState
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme
import com.hekmatullahamin.offnews.utils.NavigationConstants

/**
 * Composable function that displays the UI for an expanded screen layout, typically used for larger devices like tablets.
 *
 * This composable utilizes a `PermanentNavigationDrawer` to provide a persistent navigation drawer on the side.
 * The main content area displays either the search screen or a combination of the main screen content and news details.
 *
 * @param modifier Modifier used to adjust the layout of the screen.
 * @param onTabPressed Callback invoked when a navigation tab is pressed, providing the selected [NewsTabType].
 * @param onArticleClicked Callback invoked when an article is clicked, providing the selected [Article].
 * @param onFeaturedNewsCardClicked Callback invoked when the featured news card is clicked, providing the selected [Article].
 * @param onSearchComplete Callback invoked when the search is completed, typically used to navigate back from the search screen.
 * @param mainUiState The current UI state of the news app, containing information like the selected tab and article.
 * @param homeLazyListState The state of the LazyColumn used in the Home screen.
 * @param bookmarkLazyListState The state of the LazyColumn used in the Bookmark screen.
 */
@Composable
fun ExpandedScreen(
    modifier: Modifier = Modifier,
    onTabPressed: (NewsTabType) -> Unit,
    onArticleClicked: (Article) -> Unit = {},
    onFeaturedNewsCardClicked: (Article) -> Unit = {},
    onSearchComplete: () -> Unit = {},
    mainUiState: MainUiState,
    homeLazyListState: LazyListState,
    bookmarkLazyListState: LazyListState
) {

    val context = LocalContext.current

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(modifier = Modifier.width(dimensionResource(R.dimen.drawer_width))) {
                NavigationDrawerContent(
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.inverseSurface)
                        .padding(dimensionResource(R.dimen.drawer_padding_content)),
                    currentTab = mainUiState.currentTab,
                    onTabPressed = onTabPressed
                )
            }
        }
    ) {
        Scaffold { contentPadding ->
            Row(
                modifier = Modifier
                    .padding(contentPadding)
            ) {
                if (mainUiState.currentTab == NewsTabType.Search) {
                    SearchScreen(
                        onSearchComplete = onSearchComplete,
                        modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.screen_padding),
                            end = dimensionResource(R.dimen.screen_padding)
                        )
                    )
                } else {
                    MainScreenContent(
                        mainUiState = mainUiState,
                        homeLazyListState = homeLazyListState,
                        bookmarkLazyListState = bookmarkLazyListState,
                        onFeaturedNewsCardClicked = onFeaturedNewsCardClicked,
                        onNewsItemArrowClicked = onArticleClicked,
                        modifier = Modifier
                            .weight(1F)
                    )
                    NewsDetailsScreen(
                        articleId = mainUiState.currentArticle?.id,
                        isHomeTab = mainUiState.currentTab == NewsTabType.HOME,
                        modifier = Modifier
                            .weight(1F),
                        isFullScreen = false,
                        onBackPressed = { (context as? Activity)?.finish() }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true,
    device = "spec:width=1280dp,height=800dp, orientation=landscape"
)
@Composable
fun ExpandedScreenPreview() {
    OffNewsTheme {
        ExpandedScreen(
            onTabPressed = {},
            mainUiState = MainUiState(),
            homeLazyListState = rememberLazyListState(),
            bookmarkLazyListState = rememberLazyListState()
        )
    }
}

/**
 * Composable function that displays the main content of the screen, based on the currently selected tab.
 *
 * This function conditionally renders either the Home screen, Bookmark screen.
 *
 * @param mainUiState The current UI state of the news app, containing information like the selected tab and article.
 * @param homeLazyListState The state of the LazyColumn used in the Home screen.
 * @param bookmarkLazyListState The state of the LazyColumn used in the Bookmark screen.
 * @param onFeaturedNewsCardClicked Callback invoked when the featured news card is clicked, providing the selected [Article].
 * @param onNewsItemArrowClicked Callback invoked when an article is clicked, providing the selected [Article].
 * @param modifier Modifier used to adjust the layout of the content.
 */
@Composable
fun MainScreenContent(
    mainUiState: MainUiState,
    homeLazyListState: LazyListState,
    bookmarkLazyListState: LazyListState,
    onFeaturedNewsCardClicked: (Article) -> Unit,
    onNewsItemArrowClicked: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    when (mainUiState.currentTab) {
        NewsTabType.HOME -> {
            HomeScreen(
                lazyListState = homeLazyListState,
                onFeaturedNewsCardClicked = onFeaturedNewsCardClicked,
                onArticleActionClicked = onNewsItemArrowClicked,
//                We are expanded mode so it should not show topAppBar in HomeScreen
                isExpandedScreen = true,
                modifier = modifier
            )
        }

        NewsTabType.Bookmark -> {
            BookmarkScreen(
                lazyListState = bookmarkLazyListState,
                onArticleActionClicked = onNewsItemArrowClicked,
                modifier = modifier.padding(
                    start = dimensionResource(R.dimen.screen_padding),
                    end = dimensionResource(R.dimen.screen_padding)
                )
            )
        }

        NewsTabType.Search -> {
//            We don't show anything in the middle if the tab is Search
//            We already handled it in ExpandScreen in Scaffold block
        }
    }
}

/**
 * Composable function that displays the content of the navigation drawer.
 *
 * This function renders a list of navigation items, allowing users to switch between different sections of the app.
 *
 * @param modifier Modifier used to adjust the layout of the drawer content.
 * @param currentTab The currently selected tab, represented by a [NewsTabType].
 * @param onTabPressed Callback invoked when a navigation tab is pressed, providing the selected [NewsTabType].
 * @param navigationItemContentList A list of [NavigationItemContent] objects representing the navigation items to display.
 */
@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    currentTab: NewsTabType = NewsTabType.HOME,
    onTabPressed: ((NewsTabType) -> Unit) = {},
    navigationItemContentList: List<NavigationItemContent> = NavigationConstants.expandedScreenNavigationItems
) {
    Box(modifier = modifier) {
        NavigationDrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(R.dimen.navigation_drawer_item_vertical_space),
                alignment = Alignment.CenterVertically
            )
        ) {
            for (navItem in navigationItemContentList) {
                NavigationDrawerItem(
                    selected = currentTab == navItem.newsTabType,
                    onClick = { onTabPressed(navItem.newsTabType) },
                    icon = {
                        Icon(
                            painter = painterResource(navItem.iconResId),
                            contentDescription = stringResource(id = navItem.textResId)
                        )
                    },
                    label = {
                        Text(text = stringResource(navItem.textResId))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationDrawerContentPreview() {
    OffNewsTheme {
        NavigationDrawerContent()
    }
}

/**
 * Composable function that displays the header of the navigation drawer.
 *
 * This header typically includes the app name and logo.
 *
 * @param modifier Modifier used to adjust the layout of the header.
 */
@Composable
fun NavigationDrawerHeader(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
//            We chose this color because we chose the drawer bg color inverseSurface
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
        Image(
            painter = painterResource(R.drawable.offnews_logo), contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(dimensionResource(R.dimen.navigation_drawer_header_image_size))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationDrawerHeaderPreview() {
    OffNewsTheme {
        NavigationDrawerHeader(modifier = Modifier.fillMaxWidth())
    }
}





