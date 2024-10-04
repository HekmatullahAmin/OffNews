package com.hekmatullahamin.offnews.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.ui.composables.GradientNewsImage
import com.hekmatullahamin.offnews.ui.composables.NewsIconButton
import com.hekmatullahamin.offnews.ui.composables.rememberShimmerBrush
import com.hekmatullahamin.offnews.ui.composables.calculateTextHeightDp
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme

/**
 * Composable function that displays a loading screen for the home section.
 *
 * This screen is shown while the home content is being loaded.
 * It displays placeholder cards for featured news and regular news items
 * to indicate loading progress.
 *
 * @param modifier Modifier used to adjust the layout of the loading screen.
 */
@Composable
fun HomeLoadingScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        LoadingFeaturedNewsCard(modifier = Modifier.height(dimensionResource(R.dimen.featured_card_height)))

        Column(
            modifier = modifier
        ) {
            repeat(5) {
                LoadingNewsItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.news_item_card_height))
                        .padding(dimensionResource(R.dimen.small_padding))
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeLoadingScreenPreview() {
    OffNewsTheme {
        HomeLoadingScreen(modifier = Modifier.fillMaxSize())
    }
}

/**
 * Composable function that displays a placeholder for the featured news card.
 *
 * This card is shown while the featured news content is being loaded.
 * It uses a shimmer effect to simulate loading progress.
 *
 * @param modifier Modifier used to adjust the layout of the placeholder card.
 */
@Composable
fun LoadingFeaturedNewsCard(modifier: Modifier = Modifier) {
    val shimmerBrush = rememberShimmerBrush()
//    These are the type of our font in our Featured card. we want to know the height of the text
//    so that we can set the height of the loading card accordingly.
    val newsItemTitleFontSizeDp = calculateTextHeightDp(MaterialTheme.typography.bodyMedium)
    val newsItemSourceNameFontSizeDp = calculateTextHeightDp(MaterialTheme.typography.headlineSmall)
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading_img_content_description),
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .background(color = Color.Gray.copy(alpha = 0.5F))
                .align(Alignment.BottomStart)
                .padding(dimensionResource(R.dimen.small_padding))
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(newsItemSourceNameFontSizeDp)
                    .width(dimensionResource(R.dimen.loading_featured_card_source_name_width))
                    .drawBehind { drawRect(shimmerBrush) }
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))
            Box(
                modifier = Modifier
                    .height(newsItemTitleFontSizeDp)
                    .width(dimensionResource(R.dimen.loading_featured_card_title_name_width))
                    .drawBehind { drawRect(shimmerBrush) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingFeaturedNewsCardPreview() {
    OffNewsTheme {
        LoadingFeaturedNewsCard(modifier = Modifier.height(dimensionResource(R.dimen.featured_card_height)))
    }
}

/**
 * Composable function that displays a placeholder for a news item card.
 *
 * This card is shown while the news item content is being loaded.
 * It uses a shimmer effect to simulate loading progress.
 *
 * @param modifier Modifier used to adjust the layout of the placeholder card.
 */
@Composable
fun LoadingNewsItemCard(modifier: Modifier = Modifier) {
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
                        Image(
                            painter = painterResource(R.drawable.loading_img),
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.news_item_card_width))
                        .clip(shape = RoundedCornerShape(topEnd = dimensionResource(R.dimen.news_item_card_image_top_corner)))
                )
                LoadingNewsItemCardContent(
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.news_item_card_content_end_padding))
                )
            }
        }
        NewsIconButton(
            onClick = {},
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
fun LoadingNewsItemCardPreview() {
    OffNewsTheme {
        LoadingNewsItemCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.news_item_card_height))
        )
    }
}

/**
 * Composable function that displays the content of a loading news item card.
 *
 * This function is responsible for displaying the placeholder elements
 * for the title, source name, and date of a news item.
 * It uses a shimmer effect to simulate loading progress.
 *
 * @param modifier Modifier used to adjust the layout of the content.
 */
@Composable
fun LoadingNewsItemCardContent(modifier: Modifier = Modifier) {
    val shimmer = rememberShimmerBrush()

//    These are the type of our font in our NewsItemCardContent. we want to know the height of the text
//    so that we can set the height of the loading card accordingly.
    val newsItemTitleFontSizeDp = calculateTextHeightDp(MaterialTheme.typography.titleMedium)
    val newsItemSourceNameFontSizeDp = calculateTextHeightDp(MaterialTheme.typography.labelMedium)
    val newsItemDateFontSizeDp = calculateTextHeightDp(MaterialTheme.typography.labelSmall)

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_vertical_space)),
        modifier = modifier.padding(dimensionResource(R.dimen.small_padding))
    ) {

        Box(modifier = Modifier
            .height(newsItemTitleFontSizeDp)
            .width(dimensionResource(R.dimen.loading_news_item_card_title_width))
            .drawBehind { drawRect(shimmer) }
        )
        Box(
            modifier = Modifier
                .height(newsItemSourceNameFontSizeDp)
                .width(dimensionResource(R.dimen.loading_news_item_card_source_name_width))
                .drawBehind { drawRect(shimmer) }
        )
        Box(modifier = Modifier
            .height(newsItemDateFontSizeDp)
            .width(dimensionResource(R.dimen.loading_news_item_card_date_width))
            .drawBehind { drawRect(shimmer) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingNewsItemCardContentPreview() {
    OffNewsTheme {
        LoadingNewsItemCardContent()
    }
}