package com.hekmatullahamin.offnews.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.ui.theme.primaryDark
import com.hekmatullahamin.offnews.ui.theme.primaryLight

private const val SHIMMER_ANIMATION_DURATION = 1500
private const val SHIMMER_WIDTH = 1000f

/**
 * Creates and remembers a shimmer brush for creating a shimmering effect.
 *
 * This function creates a linear gradient brush that animates infinitely,
 * producing a shimmering effect. It uses the primary dark and light colors
 * for the gradient.
 *
 * @return A [Brush] representing the shimmer effect.
 */
@Composable
fun rememberShimmerBrush(): Brush {
    val gradientColors = listOf(
        primaryDark,
        primaryLight
    )

//    Infinite transition to animate the position of the brush
    val infiniteTransition = rememberInfiniteTransition(label = "Infinite transition")

//    Animate a float value from 0f to 1f for the brush movement
    val shimmerAnimationProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = SHIMMER_ANIMATION_DURATION, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "Shimmer animation"
    )

//    Define the animated brush with linear gradient
    return Brush.linearGradient(
        colors = gradientColors,
        start = Offset.Zero,
        end = Offset(x = shimmerAnimationProgress.value * SHIMMER_WIDTH, y = 0f)
    )
}


/**
 * Calculates the height of text in dp based on its TextStyle.
 *
 * This function converts the font size from sp to dp using the current
 * density, providing the height of the text in dp.
 *
 * @param textStyle The TextStyle of the text.
 * @return The height of the text in dp.
 */
@Composable
fun calculateTextHeightDp(textStyle: TextStyle): Dp =
    with(LocalDensity.current) { textStyle.fontSize.toDp() }


/**
 * Creates a jumping animation using an [Animatable].
 *
 * This function creates an infinite, repeatable bouncing animation that moves a value
 * between an initial and a target value. It uses a tween animation with an ease-in-out
 * cubic easing function.
 *
 * @param initialValue The initial value of the animation. Defaults to 0f.
 * @param targetValue The target value of the animation. Defaults to -50f.
 * @param durationMillis The duration of the animation in milliseconds. Defaults to 600.
 * @return The current value of the animation.
 */
@Composable
fun jumpingAnimation(
    initialValue: Float = 0f,
    targetValue: Float = -50f,
    durationMillis: Int = 600
): Float {
    val jumpOffset = remember { Animatable(initialValue) }
    LaunchedEffect(Unit) {
        jumpOffset.animateTo(
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis, easing = EaseInOutCubic),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    return jumpOffset.value
}

/**
 * Composable function that displays an icon button for news items.
 *
 * This button is used for actions related to news items, such as viewing details.
 *
 * @param modifier Modifier used to adjust the layout of the button.
 * @param contentDescription The content description for accessibility.
 * @param icon The resource ID of the icon to display.
 * @param iconInnerPadding The inner padding of the icon.
 * @param onClick Callback invoked when the button is clicked.
 */
@Composable
fun NewsIconButton(
    modifier: Modifier = Modifier,
    contentDescription: String,
    icon: Int,
    iconInnerPadding: Int,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(dimensionResource(iconInnerPadding)),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

/**
 * Composable function that displays a news image.
 *
 * This function loads and displays an image from a URL using AsyncImage.
 *
 * @param imageUrl The URL of the image to load.
 * @param modifier Modifier used to adjust the layout of the image.
 */
@Composable
fun NewsImage(imageUrl: String?, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.news_image_content_description),
        contentScale = ContentScale.FillBounds,
        error = painterResource(R.drawable.offnews_error_image),
        placeholder = painterResource(R.drawable.loading_img),
        modifier = modifier
    )
}

/**
 * Composable function that displays a news image with a gradient overlay.
 *
 * This function applies a vertical gradient to the image to create a visual effect.
 *
 * @param modifier Modifier used to adjust the layout of the image.
 * @param imageContent The content of the image to display.
 */
@Composable
fun GradientNewsImage(
    modifier: Modifier = Modifier,
    imageContent: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        imageContent()
        Box(
            modifier = Modifier
                .fillMaxSize() // Cover the entire image with the gradient
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent
                        ),
                        startY = Float.POSITIVE_INFINITY,
                        endY = 0f
                    ),
                    alpha = 0.5f
                )
        )
    }
}


