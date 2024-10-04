package com.hekmatullahamin.offnews.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.data.model.Article
import com.hekmatullahamin.offnews.ui.uistate.ScreenState
import com.hekmatullahamin.offnews.ui.composables.GradientNewsImage
import com.hekmatullahamin.offnews.ui.composables.NewsIconButton
import com.hekmatullahamin.offnews.ui.composables.NewsImage
import com.hekmatullahamin.offnews.ui.theme.Gold
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme
import com.hekmatullahamin.offnews.ui.viewmodel.NewsDetailsScreenViewModel

/**
 * Composable function that displays the News Details screen.
 *
 * This screen shows the details of a selected news article, including the image, title,
 * source, date, and description. It also provides a bookmark button to allow users
 * to save or remove articles from their bookmarks.
 *
 * @param modifier Modifier used to adjust the layout of the screen.
 * @param articleId The ID of the article to display.
 * @param isHomeTab Indicates whether the screen is launched from the Home tab.
 * @param isFullScreen Indicates whether the screen is in full-screen mode.
 * @param onBackPressed Callback invoked when the back button is pressed.
 */
@Composable
fun NewsDetailsScreen(
    modifier: Modifier = Modifier,
    articleId: Int?,
    isHomeTab: Boolean,
    isFullScreen: Boolean,
    onBackPressed: () -> Unit = {},
) {

    BackHandler {
        onBackPressed()
    }

    val viewModel: NewsDetailsScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

//    When the user click an article the getArticle will be triggered and UI state will change
//    When the user tab another tab the getArticle will be triggered and UI state will change
//    if we toggle bookmark then the
    LaunchedEffect(articleId, isHomeTab) {
        viewModel.getArticle(articleId ?: -1, isHomeTab)
    }


    when (uiState.screenState) {
        is ScreenState.Loading -> {

        }

        is ScreenState.Success -> {
            Box(modifier = modifier) {
                GradientNewsImage(
                    imageContent = {
                        NewsImage(
                            imageUrl = uiState.article?.imageUrl,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5F)
                )
                // News content slightly above the halfway point (100dp above half screen)
                uiState.article?.let { article ->
                    NewsContentWithBookmark(
                        article = article,
                        onBookmarkClicked = {
                            viewModel.toggleBookmark(it)
                        },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(
                                y = ((LocalConfiguration.current.screenHeightDp / 2).dp - dimensionResource(
                                    R.dimen.above_half_screen_offset
                                ))
                            ) // 100dp above the half screen
                    )
                }
                //        isFullScreen means that it cover all the screen and is not in expanded mode
                if (isFullScreen) {
                    NewsIconButton(
                        contentDescription = stringResource(R.string.back_button_content_description),
                        icon = R.drawable.baseline_arrow_back_24,
                        iconInnerPadding = R.dimen.icon_button_inner_padding,
                        onClick = onBackPressed,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(dimensionResource(R.dimen.back_button_outside_padding))
                    )
                }
            }
        }

        is ScreenState.Error -> {
            //    If we are in portrait mode and we entered Details Screen from Bookmark Screen and
            //    when there is no bookmarked article anymore then it should go back to Bookmark Screen
            //    after user remove bookmarked articles and reaches a point where there is no bookmarked article
            LaunchedEffect(Unit) {
                if (isFullScreen) {
                    onBackPressed()
                }
            }
            Log.d("Tag-DetailsScreen-Error", "noting found")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsDetailsScreenPreview() {
    OffNewsTheme {
        NewsDetailsScreen(
//            article = Article(
//                id = 1,
//                sourceName = "BBC",
//                title = "Volvo is giving itself another decade before it commits to selling only EVs",
//                description = "Volvo says it will only sell electric vehicles by 2040, after previously committing to a deadline of 2030. Now the company says it will sell more hybrids in the next decade.",
//                imageUrl = "https://cdn.vox-cdn.com/thumbor/0BOP11zsuTK8SoAFqqN_INQsFW4=/0x0:5100x3400/1200x628/filters:focal(2550x1700:2551x1701)/cdn.vox-cdn.com/uploads/chorus_asset/file/25603299/332052_New_Volvo_EX90_on_the_road.jpg",
//                publishedDate = "2024-09-04T16:35:55Z",
//                isBookmarked = false
//            ),
            isFullScreen = false,
//            onBookmarkClicked = {},
            articleId = 1,
            isHomeTab = true
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun NewsDetailsScreenExpandedPreview() {
    OffNewsTheme {
        NewsDetailsScreen(
//            article = Article(
//                id = 1,
//                sourceName = "BBC",
//                title = "Volvo is giving itself another decade before it commits to selling only EVs",
//                description = "Volvo says it will only sell electric vehicles by 2040, after previously committing to a deadline of 2030. Now the company says it will sell more hybrids in the next decade.",
//                imageUrl = "https://cdn.vox-cdn.com/thumbor/0BOP11zsuTK8SoAFqqN_INQsFW4=/0x0:5100x3400/1200x628/filters:focal(2550x1700:2551x1701)/cdn.vox-cdn.com/uploads/chorus_asset/file/25603299/332052_New_Volvo_EX90_on_the_road.jpg",
//                publishedDate = "2024-09-04T16:35:55Z",
//                isBookmarked = false
//            ),
            isFullScreen = true,
//            onBookmarkClicked = {},
            articleId = 1,
            isHomeTab = true
        )
    }
}

/**
 * Composable function that displays the news content with a bookmark button.
 *
 * This function combines the [DetailsNewsContentCard] and a bookmark [IconButton]
 * to provide a unified view of the news content and the bookmark action.
 *
 * @param modifier Modifier used to adjust the layout of the content.
 * @param onBookmarkClicked Callback invoked when the bookmark button is clicked.
 * @param article The [Article] to display.
 */
@Composable
fun NewsContentWithBookmark(
    modifier: Modifier = Modifier,
    onBookmarkClicked: (Article) -> Unit = {},
    article: Article,
) {
    Box(modifier = modifier) {
        DetailsNewsContentCard(article = article, modifier = Modifier.fillMaxSize())
        IconButton(
            onClick = { onBookmarkClicked(article) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = dimensionResource(R.dimen.horizontal_padding))
                .offset(y = (-25).dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_bookmarks_24),
                contentDescription = if (article.isBookmarked) stringResource(R.string.remove_bookmark_content_description)
                else stringResource(R.string.add_bookmark_content_description),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(dimensionResource(R.dimen.icon_button_inner_padding)),
                tint = if (article.isBookmarked) Gold
                else MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsContentWithBookmarkPreview() {
    OffNewsTheme {
        NewsContentWithBookmark(
            article = Article(
                id = 1,
                sourceName = "BBC",
                title = "Volvo is giving itself another decade before it commits to selling only EVs",
                description = "Volvo says it will only sell electric vehicles by 2040, after previously committing to a deadline of 2030. Now the company says it will sell more hybrids in the next decade.",
                imageUrl = "https://cdn.vox-cdn.com/thumbor/0BOP11zsuTK8SoAFqqN_INQsFW4=/0x0:5100x3400/1200x628/filters:focal(2550x1700:2551x1701)/cdn.vox-cdn.com/uploads/chorus_asset/file/25603299/332052_New_Volvo_EX90_on_the_road.jpg",
                publishedDate = "2024-09-04T16:35:55Z",
                isBookmarked = false
            )
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun NewsContentWithBookmarkExpandedPreview() {
    OffNewsTheme {
        NewsContentWithBookmark(
            article = Article(
                id = 1,
                sourceName = "BBC",
                title = "Volvo is giving itself another decade before it commits to selling only EVs",
                description = "Volvo says it will only sell electric vehicles by 2040, after previously committing to a deadline of 2030. Now the company says it will sell more hybrids in the next decade.",
                imageUrl = "https://cdn.vox-cdn.com/thumbor/0BOP11zsuTK8SoAFqqN_INQsFW4=/0x0:5100x3400/1200x628/filters:focal(2550x1700:2551x1701)/cdn.vox-cdn.com/uploads/chorus_asset/file/25603299/332052_New_Volvo_EX90_on_the_road.jpg",
                publishedDate = "2024-09-04T16:35:55Z",
                isBookmarked = false
            )
        )
    }
}

/**
 * Composable function that displays the details of a news article in a card.
 *
 * This card shows the title, source, date, and description of the article.
 *
 * @param article The [Article] to display.
 * @param modifier Modifier used to adjust the layout of the card.
 */
@Composable
fun DetailsNewsContentCard(
    article: Article,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = dimensionResource(R.dimen.details_news_content_card_top_end_corner),
            bottomEnd = 0.dp
        ),
        modifier = modifier,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_vertical_space)),
            modifier = Modifier.padding(dimensionResource(R.dimen.large_padding))
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.sourceName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.horizontal_space)))
                Text(
//                    TODO: maybe use DateFormatter here to format the date
                    text = article.publishedDate,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = article.description,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsNewsContentPreview() {
    OffNewsTheme {
        DetailsNewsContentCard(
            article = Article(
                id = 1,
                sourceName = "BBC",
                title = "Volvo is giving itself another decade before it commits to selling only EVs",
                description = "Volvo says it will only sell electric vehicles by 2040, after previously committing to a deadline of 2030. Now the company says it will sell more hybrids in the next decade.",
                imageUrl = "https://cdn.vox-cdn.com/thumbor/0BOP11zsuTK8SoAFqqN_INQsFW4=/0x0:5100x3400/1200x628/filters:focal(2550x1700:2551x1701)/cdn.vox-cdn.com/uploads/chorus_asset/file/25603299/332052_New_Volvo_EX90_on_the_road.jpg",
                publishedDate = "2024-09-04T16:35:55Z",
                isBookmarked = false
            )
        )
    }
}