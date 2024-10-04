package com.hekmatullahamin.offnews.ui

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import com.hekmatullahamin.offnews.ui.model.NewsTabType
import com.hekmatullahamin.offnews.ui.navigation.BookmarkDestination
import com.hekmatullahamin.offnews.ui.navigation.HomeDestination
import com.hekmatullahamin.offnews.ui.navigation.SearchDestination
import com.hekmatullahamin.offnews.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * A class that holds the state of the OffNews application.
 *
 * This class is responsible for managing the application's state, including:
 * - Network connectivity status
 * - Whether to show the bottom bar or navigation rail
 * - Navigation between different screens
 *
 * @property navController The [NavHostController] used for navigation.
 */
@Stable
class OffNewsAppState(
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    private val windowSizeClass: WindowSizeClass,
    val navController: NavHostController
) {

    /**
     * A [StateFlow] that emits `true` if the device is offline, `false` otherwise.
     */
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    /**
     * A boolean value indicating whether the bottom bar should be shown.
     *
     * The bottom bar is shown when the window width size class is compact or
     * the window height size class is compact.
     */
    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    /**
     * A boolean value indicating whether the navigation rail should be shown.
     *
     * The navigation rail is shown when the bottom bar is not shown.
     */
    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    /**
     * Navigates to the specified tab.
     *
     * @param tab The [NewsTabType] to navigate to.
     */
    fun navigateToTab(tab: NewsTabType) {
        val route = when (tab) {
            NewsTabType.HOME -> HomeDestination.route
            NewsTabType.Bookmark -> BookmarkDestination.route
            NewsTabType.Search -> SearchDestination.route
        }
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true // Avoid recreating the same destination
            restoreState = true
        }
    }
}

/**
 * Remembers and provides an instance of [OffNewsAppState].
 *
 * @param coroutineScope The [CoroutineScope] to use for the state flow.
 * @param networkMonitor The [NetworkMonitor] to use for network connectivity status.
 * @param windowSizeClass The [WindowSizeClass] to use for determining whether to show the bottom bar or navigation rail.
 * @param navController The [NavHostController] used for navigation.
 * @return An instance of [OffNewsAppState].
 */
@Composable
fun rememberOffNewsAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    networkMonitor: NetworkMonitor,
    windowSizeClass: WindowSizeClass,
    navController: NavHostController
): OffNewsAppState {
    return remember(
        coroutineScope,
        networkMonitor,
        windowSizeClass,
        navController
    ) {
        OffNewsAppState(
            coroutineScope,
            networkMonitor,
            windowSizeClass,
            navController
        )
    }
}