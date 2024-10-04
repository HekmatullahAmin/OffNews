package com.hekmatullahamin.offnews.ui.navigation

/**
 * Represents the destinations for navigation within the application.
 *
 * Each destination is defined by a unique route string.
 */
interface NavigationDestinations {
    /**
     * The route string representing the destination.
     */
    val route: String
}

/**
 * Represents the Home destination.
 */
object HomeDestination : NavigationDestinations {
    override val route: String = "home"
}

/**
 * Represents the Bookmark destination.
 */
object BookmarkDestination : NavigationDestinations {
    override val route: String = "bookmark"
}

/**
 * Represents the Search destination.
 */
object SearchDestination : NavigationDestinations {
    override val route: String = "search"
}

/**
 * Represents the Details destination, which requires an article ID.
 */
object DetailsDestination : NavigationDestinations {

    /**
     * The argument name for the article ID.
     */
    override val route: String = "details"

    /**
     * The route string with a placeholder for the article ID.
     */
    const val articleIdArg = "articleId"
    val routeWithArgs = "$route/{$articleIdArg}"

    /**
     * Creates the route string for the Details destination with the given article ID.
     *
     * @param articleId The ID of the article to navigate to.
     * @return The route string for the Details destination.
     */
    fun createRoute(articleId: Int): String = "$route/$articleId"
}

