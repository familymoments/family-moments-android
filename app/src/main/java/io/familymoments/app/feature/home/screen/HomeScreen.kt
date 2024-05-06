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
import androidx.compose.foundation.lazy.LazyListState
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
import io.familymoments.app.core.component.popup.CompletePopUp
import io.familymoments.app.core.component.popup.DeletePopUp
import io.familymoments.app.core.component.popup.ReportPopUp
import io.familymoments.app.core.network.dto.response.Post
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.home.uistate.HomeUiState
import io.familymoments.app.feature.home.uistate.PostPopupType
import io.familymoments.app.feature.home.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navigateToPostDetail: (Int) -> Unit,
    navigateToPostEdit: (Post) -> Unit
) {
    val homeUiState = viewModel.homeUiState.collectAsStateWithLifecycle().value
    val posts = homeUiState.posts
    val popup = homeUiState.popup

    val lazyListState = rememberLazyListState()
    val isScrolledToLast by remember(lazyListState.canScrollForward) {
        if (posts.isEmpty()) {
            mutableStateOf(false)
        } else {
            mutableStateOf(!lazyListState.canScrollForward)
        }
    }

    LaunchedEffectSetupData(viewModel::getNicknameDday, viewModel::getPosts)
    LaunchedEffectShowPopup(popup, viewModel::deletePost, viewModel::dismissPopup)
    LaunchedEffectLoadMorePostsIfScrolledToLast(isScrolledToLast, viewModel::loadMorePosts)

    HomeScreenUI(
        lazyListState = lazyListState,
        modifier = modifier,
        homeUiState = homeUiState,
        navigateToPostDetail = navigateToPostDetail,
        navigateToPostEdit = navigateToPostEdit,
        deletePostLoves = viewModel::deletePostLoves,
        postPostLoves = viewModel::postPostLoves,
        showReportPostPopup = viewModel::showReportPostPopup,
        showDeletePostPopup = viewModel::showDeletePostPopup
    )

}

@Composable
private fun LaunchedEffectSetupData(getNicknameDday: () -> Unit, getPosts: () -> Unit) {
    LaunchedEffect(Unit) {
        getNicknameDday()
        getPosts()
    }
}

@Composable
private fun LaunchedEffectShowPopup(
    popup: PostPopupType?,
    deletePost: (Long) -> Unit,
    dismissPopup: () -> Unit
) {
    val showPopup = remember { mutableStateOf(false) }

    LaunchedEffect(popup) {
        // popup이 null이 아닐 때만 show popup
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
private fun LaunchedEffectLoadMorePostsIfScrolledToLast(isScrolledToLast: Boolean, loadMorePosts: () -> Unit) {
    LaunchedEffect(isScrolledToLast) {
        if (isScrolledToLast) {
            loadMorePosts()
        }
    }
}

@Composable
fun HomeScreenUI(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    navigateToPostDetail: (Int) -> Unit = {},
    navigateToPostEdit: (Post) -> Unit = {},
    deletePostLoves: (Long) -> Unit = {},
    postPostLoves: (Long) -> Unit = {},
    showDeletePostPopup: (Long) -> Unit = {},
    showReportPostPopup: (Long) -> Unit = {}
) {
    if (homeUiState.isLoading != false) {
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
        if (homeUiState.hasNoPost) {
            Column(
                modifier = modifier.padding(horizontal = 16.dp),
            ) {
                HomeScreenTitle(hasNoPost = true, nickname = homeUiState.userNickname, dday = homeUiState.dday)
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
                    HomeScreenTitle(hasNoPost = false, nickname = homeUiState.userNickname, dday = homeUiState.dday)
                }
                items(
                    items = homeUiState.posts,
                    key = { it.postId }
                ) { post ->
                    PostItem(
                        userNickname = homeUiState.userNickname,
                        post = post,
                        navigateToPostDetail = navigateToPostDetail,
                        navigateToEditPost = navigateToPostEdit,
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
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun HomeScreenTitle(
    hasNoPost: Boolean,
    nickname: String,
    dday: String
) {
    Spacer(modifier = Modifier.height(69.dp))
    Text(
        text = stringResource(id = R.string.home_greeting_1, nickname),
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
                    append(stringResource(id = R.string.home_greeting_3, dday))
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
    val lazyListState = rememberLazyListState()
    val homeUiState = HomeUiState(
        isSuccess = true,
        isLoading = false,
        userNickname = "test",
        dday = "1",
        posts = List(10) {
            Post(
                postId = it.toLong(),
                writer = "test$it",
                profileImg = "",
                content = "test",
                imgs = listOf(),
                createdAt = "2023-03-12",
                loved = false
            )
        }
    )
    HomeScreenUI(
        lazyListState = lazyListState,
        homeUiState = homeUiState
    )
}
