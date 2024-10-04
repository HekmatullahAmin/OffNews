package com.hekmatullahamin.offnews.ui.screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme
import com.hekmatullahamin.offnews.ui.viewmodel.HomeScreenViewModel


/**
 * Composable function that displays the Home screen.
 *
 * This screen shows the list of news articles, including a pinned featured news card.
 * It handles different screen states: loading, success, and error.
 *
 * @param modifier Modifier used to adjust the layout of the screen.
 * @param lazyListState The state of the LazyColumn used for displaying the articles.
 * @param onArticleActionClicked Callback invoked when an action is performed on an article.
 * @param onSearchClicked Callback invoked when the search icon is clicked.
 * @param onFeaturedNewsCardClicked Callback invoked when the featured news card is clicked.
 * @param isExpandedScreen Indicates whether the screen is in expanded mode (e.g., on a tablet).
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    onArticleActionClicked: (Article) -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onFeaturedNewsCardClicked: (Article) -> Unit = {},
    isExpandedScreen: Boolean = false
) {
//    TODO: should only move from Search to Home if the screen is loaded
//     what is the reason that after searching if i direclty go back again to search screen fast it goes back to HomeScreen
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    BackHandler {
        (context as? Activity)?.finish()
    }

    when (uiState.screenState) {
        is ScreenState.Loading -> HomeLoadingScreen(modifier = modifier)
        is ScreenState.Success -> {
            Column(modifier = modifier) {
                if (!isExpandedScreen) {
                    NewsTopBar(
                        modifier = Modifier.fillMaxWidth(),
                        //            opens the Search Screen
                        onSearchClicked = onSearchClicked
                    )
                }
                uiState.currentSelectedArticle?.let { currentSelectedArticle ->
                    NewsListWithPinnedFeatured(
                        currentlySelectedArticle = currentSelectedArticle,
                        lazyListState = lazyListState,
                        articleList = uiState.articles,
                        onFeaturedNewsCardClicked = {
                            viewModel.setCurrentlySelectedArticle(it)
                            onFeaturedNewsCardClicked(it)
                        },
                        onArrowClicked = {
                            viewModel.setCurrentlySelectedArticle(it)
                            onArticleActionClicked(it)
                        }
                    )
                }
            }
        }

        is ScreenState.Error -> {
            HomeErrorScreen(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    OffNewsTheme {
        HomeScreen(lazyListState = rememberLazyListState())
    }
}

/**
 * Composable function that displays a list of news articles with a pinned featured news card.
 *
 * This function handles the layout and arrangement of the news articles, including the
 * pinned featured card at the top. It also manages the scroll state and updates the
 * featured news card based on the currently visible item.
 *
 * @param modifier Modifier used to adjust the layout of the list.
 * @param lazyListState The state of the LazyColumn used for displaying the articles.
 * @param currentlySelectedArticle The currently selected article to be displayed in the featured card.
 * @param onArrowClicked Callback invoked when the arrow icon on a news item card is clicked.
 * @param onFeaturedNewsCardClicked Callback invoked when the featured news card is clicked.
 * @param articleList The list of news articles to display.
 */
@Composable
fun NewsListWithPinnedFeatured(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    currentlySelectedArticle: Article,
    onArrowClicked: (Article) -> Unit = {},
    onFeaturedNewsCardClicked: (Article) -> Unit = {},
    articleList: List<Article>
) {

    //     Track the index of the currently visible item
    val firstVisibleItemIndex by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex }
    }

//    State Initialization with currentlySelectedArticle: The remember { mutableStateOf(currentlySelectedArticle) } initializes currentFeaturedNews only once
//    when the composable is first composed. This means if currentlySelectedArticle changes later (e.g., due to data loading),
//    the currentFeaturedNews might not get updated because the remember block doesn’t react to changes in currentlySelectedArticle after the initial composition.
//
//    Recomposition: While Jetpack Compose recomposes whenever inputs change, the remember block retains its initial value unless manually updated,
//    so currentFeaturedNews may stay stuck at the initial empty value even though currentlySelectedArticle gets updated later.
//
//    Instead of using remember { mutableStateOf(currentlySelectedArticle) }, which only captures the initial state,
//    you can use remember in combination with a LaunchedEffect or observe changes to currentlySelectedArticle directly to ensure that currentFeaturedNews
//    stays in sync with it.

//     Track the current news to display in the Featured card
    var currentFeaturedNews by remember { mutableStateOf(currentlySelectedArticle) }

    // LaunchedEffect that monitors the user's scroll state
    LaunchedEffect(firstVisibleItemIndex) {
        if (firstVisibleItemIndex > 0 && firstVisibleItemIndex < articleList.size) {
            currentFeaturedNews = articleList[firstVisibleItemIndex]
        }
    }

    // Another LaunchedEffect to track changes in currentlySelectedArticle
    LaunchedEffect(currentlySelectedArticle) {
        Log.d("Tag-HomeS", "LaunchedEff - ${currentlySelectedArticle.sourceName}")
        currentFeaturedNews = currentlySelectedArticle
    }

    Column(modifier = modifier) {
        // Pinned Featured Card at the top
        FeaturedNewsCard(
            article = currentFeaturedNews,
            onFeaturedNewsCardClicked = onFeaturedNewsCardClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.featured_card_height))
                .padding(
                    horizontal = 8.dp
                )
        )
//             The rest of the news list
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                horizontal = 7.dp
            )
        ) {
            itemsIndexed(articleList, key = { _, news -> news.id }) { index, newsItem ->
                // Determine if the current item is at the top of the list
                val isFirstVisible = index == firstVisibleItemIndex

                // Get the scroll offset for the first visible item
//                scrollOffset == 0 (fully visible)
                val scrollOffset =
                    remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }

                // Define the maximum scroll distance at which the opacity should fully change
//                how quickly the fade effect occurs.
                val maxScrollOffset = 200 // You can tweak this value

                /*First, we divide the scrollOffset by maxScrollOffset, which gives a value between 0 and 1.
                For example, if scrollOffset is 100 and maxScrollOffset is 400, the result is 0.25.

                coerceIn(0f, 1f): This function ensures that the result stays between 0 and 1.
                Even if the scrollOffset exceeds maxScrollOffset, the value will never exceed 1.
                Similarly, if the scrollOffset is negative or too small, it will be coerced to 0.

                1f - (scrollOffset / maxScrollOffset): By subtracting from 1f, we invert the value,
                so when the item is fully visible (scrollOffset == 0), fadeProgress is 1,
                and when it is scrolled out by the maxScrollOffset, fadeProgress becomes 0.
                This allows us to fade smoothly based on the scrolling distance.*/

                // Calculate alpha based on how much the first item has scrolled out of view
                val fadeProgress =
                    (1f - (scrollOffset.value / maxScrollOffset.toFloat()).coerceIn(
                        0f,
                        1f
                    )) //coerceIn This function ensures that the result stays between 0 and 1.

                /*If the item is the first visible item (isFirstVisible):
                The opacity starts at 0.7f (for the top visible item).
                fadeProgress * 0.3f adds an additional fade effect as it scrolls out.
                 The full fade range is from 0.7f (fully scrolled out) to 1f (fully visible).
                When fadeProgress is 1, the alpha is 1f (fully opaque), and when fadeProgress is 0, the alpha is 0.7f.*/
                val alpha = if (isFirstVisible) 0.7f + (fadeProgress * 0.3f) else 1f

                /*scaleX: Controls the horizontal scaling of the composable.
                A value of 1f means no scaling (original size), while a value greater than 1f increases the width, and less than 1f decreases it.
                scaleY: Controls the vertical scaling of the composable, similar to scaleX.
                A value of 1f means the original height, and values greater than 1f make it taller.*/
                // Animate the scale of the NewsItemCard
                val scale by animateFloatAsState(
                    targetValue = if (isFirstVisible) 1.014f else 1f, label = ""
                )

                // Each NewsItemCard
                NewsItemCard(
                    article = newsItem,
                    onArrowClicked = onArrowClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.news_item_card_height))
                        .padding(dimensionResource(R.dimen.small_padding))
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            alpha = alpha
                        )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NewsListWithPinnedFeaturedPreview() {
    val sampleNews = List(10) { index ->
        Article(
            id = index,
            sourceName = "News Source $index",
            title = "Title $index",
            description = "Description $index",
            imageUrl = "https://example.com/image.jpg",
            publishedDate = "2023-09-10",
            isBookmarked = false
        )
    }

    OffNewsTheme {
        NewsListWithPinnedFeatured(
            articleList = sampleNews,
            lazyListState = rememberLazyListState(),
            currentlySelectedArticle = sampleNews.first()
        )
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
fun NewsListWithPinnedFeaturedCardExpandedPreview() {
    val sampleNews = List(10) { index ->
        Article(
            id = index,
            sourceName = "News Source $index",
            title = "Title $index",
            description = "Description $index",
            imageUrl = "https://example.com/image.jpg",
            publishedDate = "2023-09-10",
            isBookmarked = false
        )
    }

    OffNewsTheme {
        NewsListWithPinnedFeatured(
            articleList = sampleNews,
            lazyListState = rememberLazyListState(),
            currentlySelectedArticle = sampleNews.first()
        )
    }
}

/**
 * Composable function that displays the top bar of the Home screen.
 *
 * This bar contains the app name and a search icon.
 *
 * @param modifier Modifier used to adjust the layout of the top bar.
 * @param onSearchClicked Callback invoked when the search icon is clicked.
 */
@Composable
fun NewsTopBar(
    modifier: Modifier = Modifier,
    onSearchClicked: () -> Unit = {}
) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Center)
        )
        IconButton(
            onClick = onSearchClicked,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_icon_content_description)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsTopBarPreview() {
    OffNewsTheme {
        NewsTopBar(modifier = Modifier.fillMaxWidth())
    }
}

/**
 * Composable function that displays a featured news card.
 *
 * This card is used to highlight a specific news article.
 *
 * @param article The news article to display.
 * @param onFeaturedNewsCardClicked Callback invoked when the card is clicked.
 * @param modifier Modifier used to adjust the layout of the card.
 */
@Composable
fun FeaturedNewsCard(
    modifier: Modifier = Modifier,
    article: Article,
    onFeaturedNewsCardClicked: (Article) -> Unit = {}
) {
    Box(modifier = modifier.clickable { onFeaturedNewsCardClicked(article) }) {
        NewsImage(article.imageUrl, modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .background(color = Color.Gray.copy(alpha = 0.5F))
                .align(Alignment.BottomStart)
                .padding(dimensionResource(R.dimen.small_padding))
                .fillMaxWidth()
        ) {
            Text(text = article.sourceName, style = MaterialTheme.typography.headlineSmall)
            Text(text = article.title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeaturedNewsCardPreview() {
    OffNewsTheme {
        FeaturedNewsCard(
            article = Article(
                id = 1,
                sourceName = "BBC",
                title = "Volvo is giving itself another decade before it commits to selling only EVs",
                description = "Volvo says it will only sell electric vehicles by 2040, after previously committing to a deadline of 2030. Now the company says it will sell more hybrids in the next decade.",
                imageUrl = "https://cdn.vox-cdn.com/thumbor/0BOP11zsuTK8SoAFqqN_INQsFW4=/0x0:5100x3400/1200x628/filters:focal(2550x1700:2551x1701)/cdn.vox-cdn.com/uploads/chorus_asset/file/25603299/332052_New_Volvo_EX90_on_the_road.jpg",
                publishedDate = "2024-09-04T16:35:55Z",
                isBookmarked = false
            ),
            modifier = Modifier.height(dimensionResource(R.dimen.featured_card_height))
        )
    }
}

/**
 * Composable function that displays a news item card.
 *
 * This card is used to display a single news article in the list.
 *
 * @param modifier Modifier used to adjust the layout of the card.
 * @param article The news article to display.
 * @param onArrowClicked Callback invoked when the arrow icon on the card is clicked.
 */
@Composable
fun NewsItemCard(
    modifier: Modifier = Modifier,
    article: Article,
    onArrowClicked: (Article) -> Unit = {}
) {
    Box(modifier = modifier) {
        Card(
            shape = RoundedCornerShape(
                topStart = 0.dp, bottomStart = 0.dp,
                topEnd = dimensionResource(R.dimen.card_top_end_corner), bottomEnd = 0.dp
            ),
            modifier = Modifier
                .padding(end = dimensionResource(R.dimen.news_item_card_end_padding))
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(dimensionResource(R.dimen.card_elevation))
        ) {
            Row {
                GradientNewsImage(
                    imageContent = {
                        NewsImage(
                            imageUrl = article.imageUrl,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.news_item_card_width))
                        .clip(shape = RoundedCornerShape(topEnd = dimensionResource(R.dimen.news_item_card_image_top_corner)))
                )
                NewsItemCardContent(
                    article, modifier = Modifier
                        .padding(end = 20.dp)
                        .padding(dimensionResource(R.dimen.small_padding))
                )
            }
        }
        NewsIconButton(
            onClick = { onArrowClicked(article) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = dimensionResource(R.dimen.news_item_card_icon_x_offset)),
            contentDescription = stringResource(R.string.news_item_card_arrow_content_description),
            icon = R.drawable.baseline_arrow_right_24,
            iconInnerPadding = R.dimen.icon_button_inner_padding
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewsItemCardPreview() {
    OffNewsTheme {
        NewsItemCard(
            article = Article(
                id = 1,
                sourceName = "BBC",
                title = "Volvo is giving itself another decade before it commits to selling only EVs",
                description = "Volvo says it will only sell electric vehicles by 2040, after previously committing to a deadline of 2030. Now the company says it will sell more hybrids in the next decade.",
                imageUrl = "https://cdn.vox-cdn.com/thumbor/0BOP11zsuTK8SoAFqqN_INQsFW4=/0x0:5100x3400/1200x628/filters:focal(2550x1700:2551x1701)/cdn.vox-cdn.com/uploads/chorus_asset/file/25603299/332052_New_Volvo_EX90_on_the_road.jpg",
                publishedDate = "2024-09-04T16:35:55Z",
                isBookmarked = false
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.news_item_card_height))
        )
    }
}

/**
 * Composable function that displays the content of a news item card.
 *
 * This function displays the title, source name, and published date of a news article.
 *
 * @param article The news article to display.
 * @param modifier Modifier used to adjust the layout of the content.
 */
@Composable
fun NewsItemCardContent(article: Article, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = article.title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = article.sourceName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = article.publishedDate,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}