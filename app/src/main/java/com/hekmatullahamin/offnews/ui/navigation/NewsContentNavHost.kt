package com.hekmatullahamin.offnews.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.ui.model.NewsTabType
import com.hekmatullahamin.offnews.ui.uistate.MainUiState
import com.hekmatullahamin.offnews.ui.screen.BookmarkScreen
import com.hekmatullahamin.offnews.ui.screen.HomeScreen
import com.hekmatullahamin.offnews.ui.screen.NewsDetailsScreen
import com.hekmatullahamin.offnews.ui.screen.SearchScreen


/**
 * Composable function that sets up the navigation host for the news content.
 *
 * This function creates a [NavHost] to manage navigation between different screens
 * within the news content area. It handles navigation to the Home, Bookmark,
 * Search, and Details screens.
 *
 * @param modifier Modifier used to adjust the layout of the navigation host.
 * @param currentTabType The currently selected [NewsTabType].
 * @param navController The [NavHostController] used for navigation.
 * @param onFeaturedNewsCardClicked Callback invoked when a featured news card is clicked.
 * @param onArticleClicked Callback invoked when an article is clicked.
 * @param homeLazyListState The [LazyListState] for the Home screen's lazy list.
 * @param bookmarkLazyListState The [LazyListState] for the Bookmark screen's lazy list.
 * @param currentRoute The current route to be used as the start destination.
 * This is important for handling navigation between compact and expanded screens,
 * ensuring the correct screen is displayed after an orientation change.
 */
@Composable
fun NewsContentNavHost(
    modifier: Modifier = Modifier,
    currentTabType: NewsTabType,
    navController: NavHostController,
    onFeaturedNewsCardClicked: (Article) -> Unit = {},
    onArticleClicked: (Article) -> Unit = {},
    homeLazyListState: LazyListState,
    bookmarkLazyListState: LazyListState,
    currentRoute: String,
) {

    NavHost(
        navController = navController,
        startDestination = currentRoute,
        modifier = modifier
    ) {
        composable(HomeDestination.route) {
            HomeScreen(
                onSearchClicked = {
                    navController.navigate(SearchDestination.route)
                },
                onFeaturedNewsCardClicked = { article ->
                    onFeaturedNewsCardClicked(article)
                    navController.navigate(DetailsDestination.createRoute(article.id))
                },
                onArticleActionClicked = { article ->
                    navController.navigate(DetailsDestination.createRoute(article.id))
                    onArticleClicked(article)
                },
                isExpandedScreen = false,
                lazyListState = homeLazyListState
            )
        }

        composable(BookmarkDestination.route) {
            BookmarkScreen(
                onArticleActionClicked = { article ->
                    navController.navigate(DetailsDestination.createRoute(article.id))
                    onArticleClicked(article)
                },
                lazyListState = bookmarkLazyListState,
                modifier = Modifier.padding(dimensionResource(R.dimen.screen_padding))
            )
        }

        composable(
            route = DetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DetailsDestination.articleIdArg) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt(DetailsDestination.articleIdArg)
            val isHomeTab = currentTabType == NewsTabType.HOME
            NewsDetailsScreen(
                articleId = articleId,
                isHomeTab = isHomeTab,
                onBackPressed = {
//                    TODO: why can't use navigateUp
//                    we navigate this way because when the orientation change from Landscape to Portrait
//                        the nav don't have idea about the route it come from, it gets null after orientation get change.
//                    so, cause of that we can't navigateUp and we navigate like this to specific screen base on the tab which currently selected
                    if (currentTabType == NewsTabType.HOME) {
                        navController.navigate(HomeDestination.route) {
                            popUpTo(HomeDestination.route)
                        }
                    } else if (currentTabType == NewsTabType.Bookmark) {
                        navController.navigate(BookmarkDestination.route) {
                            popUpTo(BookmarkDestination.route)
                        }
                    }
                },
                isFullScreen = true
            )
        }

        composable(SearchDestination.route) {
            SearchScreen(
                onSearchComplete = {
                    navController.navigateUp() // Navigate back after search is completed
                },
                modifier = Modifier.padding(dimensionResource(R.dimen.screen_padding))
            )
        }
    }
}