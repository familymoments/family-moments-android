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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
import io.familymoments.app.core.component.popup.CompletePopUp
import io.familymoments.app.core.component.popup.DeletePopUp
import io.familymoments.app.core.component.popup.ReportPopUp
import io.familymoments.app.core.network.dto.response.Post
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.calendar.viewmodel.CalendarDayViewModel
import io.familymoments.app.feature.home.uistate.PostPopupType
import java.time.LocalDate

@Composable
fun CalendarDayScreen(
    modifier: Modifier,
    viewModel: CalendarDayViewModel,
    navigateToPostDetail: (Int) -> Unit,
    navigateToPostEdit: (Post) -> Unit
) {
    val calendarDayUiState = viewModel.calendarDayUiState.collectAsStateWithLifecycle().value
    val initialDate = calendarDayUiState.selectedDate
    val posts = calendarDayUiState.posts
    val hasNoPost = calendarDayUiState.hasNoPost
    val popup = calendarDayUiState.popup

    val lazyListState = rememberLazyListState()
    val isScrolledToLast by remember(lazyListState.canScrollForward) {
        if (posts.isEmpty()) {
            mutableStateOf(false)
        } else {
            mutableStateOf(!lazyListState.canScrollForward)
        }
    }
    LaunchedEffectShowPopup(popup, viewModel::deletePost, viewModel::dismissPopup)
    LaunchedEffectLoadMorePostsIfScrolledToLast(isScrolledToLast, viewModel::loadMorePostsByDay)

    CalendarDayUI(
        lazyListState = lazyListState,
        modifier = modifier,
        userNickname = calendarDayUiState.userNickname,
        hasNoPost = hasNoPost,
        posts = posts,
        initialDate = initialDate,
        getPostsByPrevDay = viewModel::getPostsByPrevDay,
        getPostsByNextDay = viewModel::getPostsByNextDay,
        navigateToPostDetail = navigateToPostDetail,
        navigateToPostEdit = navigateToPostEdit,
        deletePostLoves = viewModel::deletePostLoves,
        postPostLoves = viewModel::postPostLoves,
        showDeletePostPopup = viewModel::showDeletePostPopup,
        showReportPostPopup = viewModel::showReportPostPopup
    )
}

@Composable
private fun LaunchedEffectLoadMorePostsIfScrolledToLast(isScrolledToLast: Boolean, loadMorePostsByDay: () -> Unit) {
    LaunchedEffect(isScrolledToLast) {
        if (isScrolledToLast) {
            loadMorePostsByDay()
        }
    }
}

@Composable
private fun LaunchedEffectShowPopup(popup: PostPopupType?, deletePost: (Long) -> Unit, dismissPopup: () -> Unit) {
    val showPopup = remember { mutableStateOf(false) }

    LaunchedEffect(popup) {
        showPopup.value = popup != null
    }

    if (showPopup.value) {
        when (popup) {
            PostPopupType.PostLovesFailure -> {
                //TODO: 좋아요 생성 실패 팝업
            }

            PostPopupType.DeleteLovesFailure -> {
                //TODO: 좋아요 삭제 실패 팝업
            }

            is PostPopupType.DeletePost -> {
                DeletePopUp(
                    content = stringResource(id = R.string.post_delete_pop_up_content),
                    delete = { deletePost(popup.postId) },
                    onDismissRequest = dismissPopup
                )
            }

            PostPopupType.DeletePostSuccess -> {
                CompletePopUp(
                    content = stringResource(R.string.post_detail_delete_complete_pop_label),
                    onDismissRequest = dismissPopup
                )
            }

            PostPopupType.DeletePostFailure -> {
                // TODO: 게시물 삭제 실패 팝업
            }

            is PostPopupType.ReportPost -> {
                ReportPopUp(
                    onDismissRequest = dismissPopup,
                    onReportRequest = {
                        // TODO: 신고하기 기능 구현
                        // viewModel.reportPost(popup.postId)
                    }
                )
            }

            PostPopupType.ReportPostSuccess -> {
                // TODO: 신고가 완료되었습니다 팝업
            }

            PostPopupType.ReportPostFailure -> {

            }

            else -> {
                // null
            }
        }
    }
}

@Composable
private fun CalendarDayUI(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    userNickname:String,
    hasNoPost: Boolean,
    posts: List<Post>,
    initialDate: LocalDate,
    getPostsByPrevDay: () -> Unit = {},
    getPostsByNextDay: () -> Unit = {},
    navigateToPostDetail: (Int) -> Unit = {},
    navigateToPostEdit: (Post) -> Unit = {},
    deletePostLoves: (Long) -> Unit = {},
    postPostLoves: (Long) -> Unit = {},
    showDeletePostPopup: (Long) -> Unit = {},
    showReportPostPopup: (Long) -> Unit = {}
) {
    Column(modifier = modifier) {
        CalendarHeader(
            formattedYearMonth = initialDate.toString().replace("-", "."),
            onClickPrevMonth = getPostsByPrevDay,
            onClickNextMonth = getPostsByNextDay
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
                items(
                    items = posts,
                    key = { it.postId }
                )
                { post ->
                    PostItem(
                        userNickname = userNickname,
                        post = post,
                        navigateToPostDetail = navigateToPostDetail,
                        navigateToEditPost = {
                            navigateToPostEdit(post)
                        },
                        onClickPostLoves = {
                            if (post.loved) {
                                deletePostLoves(post.postId)
                            } else {
                                postPostLoves(post.postId)
                            }
                        },
                        showDeletePostPopup = {
                            showDeletePostPopup(post.postId)
                        },
                        showReportPostPopup = {
                            showReportPostPopup(post.postId)
                        })
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
            .background(color = AppColors.grey9),
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
    val lazyListState = rememberLazyListState()
    CalendarDayUI(
        userNickname = "test0",
        lazyListState = lazyListState,
        hasNoPost = false,
        posts = List(10) {
            Post(
                postId = it.toLong(),
                writer = "test$it",
                profileImg = "",
                content = "test",
                imgs = listOf(),
                createdAt = "2023-03-12",
                countLove = 0,
                loved = false
            )
        },
        initialDate = LocalDate.now()
    )
}
