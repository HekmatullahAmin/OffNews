package com.hekmatullahamin.offnews.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme

/**
 * Composable function that displays a loading screen for the bookmark section.
 *
 * This screen is shown while the bookmark data is being loaded.
 * It displays a series of placeholder news item cards to indicate loading progress.
 *
 * @param modifier Modifier used to adjust the layout of the loading screen.
 */
@Composable
fun BookmarkLoadingScreen(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_vertical_space)),
        modifier = modifier
    ) {
        repeat(5) {
            LoadingNewsItemCard(modifier = Modifier.height(dimensionResource(R.dimen.news_item_card_height)))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookmarkLoadingScreenPreview() {
    OffNewsTheme {
        BookmarkLoadingScreen()
    }
}