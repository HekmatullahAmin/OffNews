package com.hekmatullahamin.offnews.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hekmatullahamin.offnews.ui.model.NewsTabType

/**
 * Represents the content of a navigation item used in navigation components
 * like bottom navigation bar, navigation rail, and permanent drawer.
 *
 * This data class holds information about a navigation item, including its
 * associated news tab type, icon resource ID, and text resource ID.
 *
 * @property newsTabType The type of news tab associated with this navigation item.
 * @property iconResId The resource ID of the icon to display for this navigation item.
 * @property textResId The resource ID of the text to display for this navigation item.
 */
data class NavigationItemContent(
    val newsTabType: NewsTabType,
    @DrawableRes val iconResId: Int,
    @StringRes val textResId: Int
)
