package io.familymoments.app.feature.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.PostItem
import io.familymoments.app.core.component.PostItemPreview
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navigateToPostDetail: (Int) -> Unit
) {
    val homeUiState = viewModel.homeUiState.collectAsStateWithLifecycle()
    val posts = homeUiState.value.posts
    val lazyListState = rememberLazyListState()
    val isScrolledToLast by remember(lazyListState.canScrollForward) {
        if (posts.isEmpty()) {
            mutableStateOf(false)
        } else {
            mutableStateOf(!lazyListState.canScrollForward)
        }
    }
    val isLoading = homeUiState.value.isLoading
    val hasNoPost = homeUiState.value.hasNoPost

    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }
    LaunchedEffect(isScrolledToLast) {
        if (isScrolledToLast) {
            viewModel.loadMorePosts()
        }
    }

    if (isLoading != false) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    } else {
        if (hasNoPost) {
            Column(
                modifier = modifier.padding(horizontal = 16.dp),
            ) {
                HomeScreenTitle(hasNoPost = true)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(56.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_no_post),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(top = 17.dp),
                        text = stringResource(id = R.string.home_content_no_post),
                        style = AppTypography.BTN5_16,
                        color = AppColors.grey2,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier,
                state = lazyListState,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                item {
                    HomeScreenTitle(hasNoPost = false)
                }
                items(posts) { post ->
                    PostItem(post = post, navigateToPostDetail = navigateToPostDetail)
                }
            }
        }
    }
}

@Composable
fun HomeScreenTitle(hasNoPost: Boolean) {
    Spacer(modifier = Modifier.height(69.dp))
    Text(
        text = stringResource(id = R.string.home_greeting_1, "딸내미"),
        style = AppTypography.B2_14,
        color = AppColors.black2
    )
    if (hasNoPost) {
        Text(
            text = stringResource(id = R.string.home_header_no_post),
            style = AppTypography.B1_16,
            color = AppColors.black2
        )
    } else {
        Text(
            text = buildAnnotatedString {
                withStyle(style = AppTypography.B1_16.toSpanStyle().copy(color = AppColors.black2)) {
                    append(stringResource(id = R.string.home_greeting_2))
                }
                append(" ")
                withStyle(style = AppTypography.H2_24.toSpanStyle().copy(color = AppColors.black2)) {
                    append(stringResource(id = R.string.home_greeting_3, 2))
                }
                append(" ")
                withStyle(style = AppTypography.B1_16.toSpanStyle().copy(color = AppColors.black2)) {
                    append(stringResource(id = R.string.home_greeting_4))
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(9.dp))
    Spacer(
        modifier = Modifier
            .width(67.dp)
            .height(1.dp)
            .background(AppColors.deepPurple1)
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        item {
            HomeScreenTitle(hasNoPost = false)
        }
        items(10) {
            PostItemPreview()
        }
    }
}
