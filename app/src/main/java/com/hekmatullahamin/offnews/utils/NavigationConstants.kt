package com.hekmatullahamin.offnews.utils

import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.ui.model.NewsTabType
import com.hekmatullahamin.offnews.ui.navigation.NavigationItemContent

object NavigationConstants {
    val NavigationItems = listOf(
        NavigationItemContent(
            newsTabType = NewsTabType.HOME,
            iconResId = R.drawable.baseline_home_filled_24,
            textResId = R.string.tab_title_home
        ),
        NavigationItemContent(
            newsTabType = NewsTabType.Bookmark,
            iconResId = R.drawable.baseline_bookmarks_24,
            textResId = R.string.tab_title_bookmark
        )
    )

    val expandedScreenNavigationItems = NavigationItems + NavigationItemContent(
        newsTabType = NewsTabType.Search,
        iconResId = R.drawable.baseline_search_24,
        textResId = R.string.tab_title_search
    )
}