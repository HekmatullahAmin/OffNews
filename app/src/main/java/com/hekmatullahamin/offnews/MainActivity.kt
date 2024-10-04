package com.hekmatullahamin.offnews

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.hekmatullahamin.offnews.ui.OffNewsAppState
import com.hekmatullahamin.offnews.ui.model.NewsTabType
import com.hekmatullahamin.offnews.ui.navigation.BookmarkDestination
import com.hekmatullahamin.offnews.ui.navigation.DetailsDestination
import com.hekmatullahamin.offnews.ui.navigation.HomeDestination
import com.hekmatullahamin.offnews.ui.navigation.SearchDestination
import com.hekmatullahamin.offnews.ui.rememberOffNewsAppState
import com.hekmatullahamin.offnews.ui.screen.BottomNavigationBar
import com.hekmatullahamin.offnews.ui.screen.CompactAndMediumScreen
import com.hekmatullahamin.offnews.ui.screen.ExpandedScreen
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme
import com.hekmatullahamin.offnews.ui.viewmodel.MainViewModel
import com.hekmatullahamin.offnews.utils.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main activity of the OffNews application.
 *
 * This activity is responsible for setting up the app's UI and handling
 * the initial fetching of top headlines.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * The network monitor for checking internet connectivity.
     */
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    /**
     * Called when the activity is created.
     *
     * This function sets up the app's UI using Jetpack Compose and initializes
     * the main ViewModel and app state. It also triggers the initial fetching
     * of top headlines if they haven't been fetched yet.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OffNewsTheme {
                val windowSize = calculateWindowSizeClass(this)
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()
                val appState = rememberOffNewsAppState(
                    networkMonitor = networkMonitor,
                    windowSizeClass = windowSize,
                    navController = navController
                )

                val snackBarHostState = remember { SnackbarHostState() }
                val isOffline by appState.isOffline.collectAsStateWithLifecycle()

                //    If user is not connected to the internet show a snack bar to inform them.
                val notConnectedMessage = stringResource(R.string.not_connected)
                LaunchedEffect(key1 = isOffline) {
                    if (isOffline) {
                        snackBarHostState.showSnackbar(
                            message = notConnectedMessage,
                            duration = SnackbarDuration.Indefinite
                        )
                    } else {
                        snackBarHostState.currentSnackbarData?.dismiss()
                        viewModel.onNetworkAvailable()
                    }
                }

                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
                OffNewsAppContent(
                    windowSize = windowSize.widthSizeClass,
                    viewModel = viewModel,
                    appState = appState,
                    snackBarHostState = snackBarHostState
                )
            }
        }
    }
}

/**
 * Composable function that represents the main content of the OffNews app.
 *
 * This function sets up the Scaffold, handles offline states with a Snackbar,
 * and displays the appropriate screen based on the window size.
 *
 * @param windowSize The width size class of the window.
 * @param viewModel The main ViewModel for the app.
 * @param appState The app state object.
 */
@Composable
fun OffNewsAppContent(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass,
    viewModel: MainViewModel,
    appState: OffNewsAppState,
    snackBarHostState: SnackbarHostState
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    // Remember LazyListState for both Home and Bookmark screens
    // This will save the scroll state across configuration changes like screen rotation
    val homeLazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    val bookmarkLazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                BottomNavigationBar(
                    currentTab = uiState.currentTab,
                    onTabPressed = { tabType ->
                        viewModel.updateSelectedTab(tabType)
                        appState.navigateToTab(tabType)
                        val route = when (tabType) {
                            NewsTabType.HOME -> HomeDestination.route
                            NewsTabType.Bookmark -> BookmarkDestination.route
//                                There is no tab for search in compact and medium screen, it is only for expanded screen.
                            NewsTabType.Search -> SearchDestination.route
                        }
                        viewModel.setCurrentRoute(route)
                    }
                )
            }
        }
    ) { contentPadding ->
        when (windowSize) {
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
                CompactAndMediumScreen(
                    currentTab = uiState.currentTab,
                    onTabPressed = { tabType ->
                        viewModel.updateSelectedTab(tabType)
                        appState.navigateToTab(tabType)
                        val route = when (tabType) {
                            NewsTabType.HOME -> HomeDestination.route
                            NewsTabType.Bookmark -> BookmarkDestination.route
                            NewsTabType.Search -> SearchDestination.route
                        }
                        viewModel.setCurrentRoute(route)
                    },
                    onFeaturedNewsCardClicked = {
                        viewModel.setCurrentRoute(DetailsDestination.createRoute(articleId = it.id))
                    },
                    onArticleClicked = { article ->
                        viewModel.setCurrentRoute(DetailsDestination.createRoute(articleId = article.id))
                    },
                    offNewsAppState = appState,
                    homeLazyListState = homeLazyListState,
                    bookmarkLazyListState = bookmarkLazyListState,
                    currentRoute = viewModel.currentScreen
                )
            }

            WindowWidthSizeClass.Expanded -> {
                ExpandedScreen(
                    onTabPressed = { tabType ->
                        val route = when (tabType) {
                            NewsTabType.HOME -> HomeDestination.route
                            NewsTabType.Bookmark -> BookmarkDestination.route
                            NewsTabType.Search -> SearchDestination.route
                        }
                        viewModel.setCurrentRoute(route)
                        viewModel.updateSelectedTab(tabType)
                    },
                    mainUiState = uiState,
                    onFeaturedNewsCardClicked = {
                        Log.d("TAG-Main", "Expand-feature-article: $it")
                        viewModel.setCurrentRoute(DetailsDestination.createRoute(articleId = it.id))
                        viewModel.updateSelectedArticle(it)
                    },
                    onArticleClicked = { article ->
                        Log.d("TAG-Main", "Expand-item-article: $article")
                        viewModel.setCurrentRoute(DetailsDestination.createRoute(articleId = article.id))
                        viewModel.updateSelectedArticle(article)
                    },
                    onSearchComplete = {
                        Log.d("TAG-Main", "Expand-search-complete")
//                    when search get completed we want to get navigate to Home screen and see the search results.
                        viewModel.setCurrentRoute(HomeDestination.route)
                        viewModel.updateSelectedTab(NewsTabType.HOME)
                    },
                    homeLazyListState = homeLazyListState,
                    bookmarkLazyListState = bookmarkLazyListState,
                    modifier = Modifier.padding(contentPadding)
                )
            }
        }
    }
}