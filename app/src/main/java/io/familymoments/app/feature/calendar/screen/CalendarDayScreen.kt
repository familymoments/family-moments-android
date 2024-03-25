package io.familymoments.app.feature.calendar.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.component.PostItem
import io.familymoments.app.core.component.PostItemPreview
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.calendar.viewmodel.CalendarDayViewModel

@Composable
fun CalendarDayScreen(
    modifier: Modifier,
    viewModel: CalendarDayViewModel,
    navigateToPostDetail: (Int) -> Unit
) {
    val calendarDayUiState = viewModel.calendarDayUiState.collectAsStateWithLifecycle()
    val initialDate = calendarDayUiState.value.selectedDate
    val posts = calendarDayUiState.value.posts
    val hasNoPost = calendarDayUiState.value.hasNoPost

    val lazyListState = rememberLazyListState()
    val isScrolledToLast by remember(lazyListState.canScrollForward) {
        if (posts.isEmpty()) {
            mutableStateOf(false)
        } else {
            mutableStateOf(!lazyListState.canScrollForward)
        }
    }

    LaunchedEffect(isScrolledToLast) {
        if (isScrolledToLast) {
            viewModel.loadMorePostsByDay()
        }
    }

    Column(modifier = modifier) {
        CalendarHeader(
            formattedYearMonth = initialDate.toString().replace("-", "."),
            onClickPrevMonth = viewModel::getPostsByPrevDay,
            onClickNextMonth = viewModel::getPostsByNextDay
        )

        if (hasNoPost) {
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
                    text = stringResource(id = R.string.calendar_day_no_post),
                    style = AppTypography.BTN5_16,
                    color = AppColors.grey2,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(posts.size) { index ->
                    PostItem(
                        post = posts[index],
                        navigateToPostDetail = navigateToPostDetail
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader(
    formattedYearMonth: String,
    onClickPrevMonth: () -> Unit,
    onClickNextMonth: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 40.dp, bottom = 11.dp)
            .padding(horizontal = 16.dp)
            .height(34.dp)
            .fillMaxWidth()
            .background(color = AppColors.deepPurple2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(modifier = Modifier.size(32.dp), onClick = onClickPrevMonth) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_previous_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = formattedYearMonth,
            style = AppTypography.SH2_18,
            color = AppColors.grey6,
            textAlign = TextAlign.Center
        )
        IconButton(modifier = Modifier.size(32.dp), onClick = onClickNextMonth) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_next_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarDayScreenPreview() {
    Column(modifier = Modifier) {
        CalendarHeader(
            formattedYearMonth = "2023.08.16",
            onClickPrevMonth = {},
            onClickNextMonth = {}
        )
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(10) {
                PostItemPreview()
            }
        }
    }
}
