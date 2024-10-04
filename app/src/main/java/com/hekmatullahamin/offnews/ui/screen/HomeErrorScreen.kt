package com.hekmatullahamin.offnews.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.ui.composables.jumpingAnimation
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme

/**
 * Composable function that displays an error screen for the home section.
 *
 * This screen is shown when there is an error loading the home content.
 * It displays an image representing the error state with a subtle jumping animation.
 *
 * @param modifier Modifier used to adjust the layout of the error screen.
 */
@Composable
fun HomeErrorScreen(modifier: Modifier = Modifier) {

    val jumpOffset = jumpingAnimation()
    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .size(
                    width = dimensionResource(R.dimen.error_image_card_width_size),
                    height = dimensionResource(R.dimen.error_image_card_height_size)
                )
                .graphicsLayer {
                    rotationX = 10f
                    // Apply the jumping translation on the Y-axis
//                    translationY = jumpOffset.value
                    translationY = jumpOffset
                },
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation))
        ) {
            Image(
                painter = painterResource(R.drawable.error_screen),
                contentDescription = stringResource(R.string.error_image_content_description),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorScreenPreview() {
    OffNewsTheme {
        HomeErrorScreen(modifier = Modifier.fillMaxSize())
    }
}