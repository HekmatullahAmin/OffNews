package com.hekmatullahamin.offnews.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hekmatullahamin.offnews.R
import com.hekmatullahamin.offnews.ui.theme.OffNewsTheme

/**
 * Composable function that displays an error screen for the bookmark section.
 *
 * This screen is shown when there are no bookmarked articles available.
 * It displays an image representing the empty state and an error message.
 *
 * @param modifier Modifier used to adjust the layout of the error screen.
 * @param errorMessage The error message to display.
 */
@Composable
fun BookmarkErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.no_bookmarks),
                contentDescription = stringResource(R.string.no_bookmarks_image_content_description),
                modifier = Modifier.size(dimensionResource(R.dimen.bookmark_error_image_size))
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.bookmark_error_image_text_spacing)))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookmarkErrorScreenPreview() {
    OffNewsTheme {
        BookmarkErrorScreen(errorMessage = "No bookmarked articles available")
    }
}