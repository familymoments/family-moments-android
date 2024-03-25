package io.familymoments.app.feature.postdetail.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.core.util.noRippleClickable
import io.familymoments.app.feature.postdetail.component.LoveListPopUp
import io.familymoments.app.feature.postdetail.component.PostDetailCompletePopUp
import io.familymoments.app.feature.postdetail.component.PostDetailExecutePopUp
import io.familymoments.app.feature.postdetail.component.ReportPopUp
import io.familymoments.app.feature.postdetail.model.component.postDetailContentShadow
import io.familymoments.app.feature.postdetail.model.response.GetCommentsResult
import io.familymoments.app.feature.postdetail.model.response.GetPostDetailResult
import io.familymoments.app.feature.postdetail.model.uistate.CommentLogics
import io.familymoments.app.feature.postdetail.model.uistate.GetPostLovesUiState
import io.familymoments.app.feature.postdetail.model.uistate.PopupUiState
import io.familymoments.app.feature.postdetail.model.uistate.PostCommentUiState
import io.familymoments.app.feature.postdetail.model.uistate.PostLogics
import io.familymoments.app.feature.postdetail.viewmodel.PostDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel,
    index: Long,
    modifier: Modifier,
    navigateToBack: () -> Unit,
    navigateToModify: (GetPostDetailResult) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getPostDetail(index)
        viewModel.getPostComments(index)
        viewModel.getPostLoves(index)
    }
    val context = LocalContext.current
    val postUiState = viewModel.postUiState.collectAsStateWithLifecycle().value
    val commentUiState = viewModel.commentUiState.collectAsStateWithLifecycle().value
    val popupUiState = viewModel.popupUiState.collectAsStateWithLifecycle().value

    if (postUiState.deletePostUiState.isSuccess == true && popupUiState.showDeleteCompletePopup) {
        PostDetailCompletePopUp(
            content = stringResource(R.string.post_detail_delete_complete_pop_label)
        ) {
            popupUiState.popupStatusLogics.showDeleteCompletePopup(false)
            navigateToBack()
        }
    }
    if (commentUiState.deleteCommentUiState.isSuccess == true && popupUiState.showDeleteCompletePopup) {
        PostDetailCompletePopUp(
            content = stringResource(R.string.post_detail_delete_complete_pop_label)
        ) { popupUiState.popupStatusLogics.showDeleteCompletePopup(false) }
    }


    if (popupUiState.executePopupUiState.show) {
        PostDetailExecutePopUp(content = popupUiState.executePopupUiState.content,
            onDismissRequest = {
                popupUiState.popupStatusLogics.showExecutePopup(false, "") {}
            },
            execute = {
                popupUiState.executePopupUiState.execute()
                popupUiState.popupStatusLogics.showExecutePopup(false, "") {}
            })
    }
    if (popupUiState.reportPopupUiState.show) {
        ReportPopUp(onDismissRequest = {
            popupUiState.popupStatusLogics.showReportPopup(false) {}
        }) {
            popupUiState.reportPopupUiState.execute()
            popupUiState.popupStatusLogics.showReportPopup(false) {}
        }
    }
    if (commentUiState.postCommentUiState.isSuccess == true || commentUiState.deleteCommentUiState.isSuccess == true) {
        commentUiState.logics.getComments(index)
    }

    val pagerState = rememberPagerState(pageCount = { postUiState.getPostDetailUiState.result.imgs.size })

    LazyColumn {
        item {
            Column(modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 26.dp, bottom = 63.dp)) {
                if (postUiState.getPostDetailUiState.isSuccess == true) {
                    WriterInfo(
                        postUiState.getPostDetailUiState.result,
                        viewModel::formatPostCreatedDate
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Divider(Modifier.height(1.dp), color = AppColors.deepPurple3)
                Spacer(modifier = Modifier.height(19.dp))
                Box(modifier = Modifier.postDetailContentShadow()) {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(AppColors.grey6)
                    ) {
                        PostPhotos(postUiState.getPostDetailUiState.result.imgs, pagerState)
                        PostContent(
                            postUiState.getPostDetailUiState.result,
                            postUiState.logics,
                            popupUiState
                        ) { navigateToModify(postUiState.getPostDetailUiState.result) }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .background(color = AppColors.grey4)
                        )
                        CommentTextField(
                            commentUiState.getCommentsUiState.result.size,
                            postUiState.getPostDetailUiState.result.postId,
                            viewModel::postComment,
                            commentUiState.postCommentUiState,
                            postUiState.getPostLovesUiState,
                            context
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        if (commentUiState.getCommentsUiState.isSuccess == true) {
                            CommentItems(
                                commentUiState.getCommentsUiState.result,
                                commentUiState.logics,
                                viewModel::formatCommentCreatedDate,
                                popupUiState
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                }
            }
        }

    }

}


fun showToastMessage(context: Context, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun WriterInfo(postInfo: GetPostDetailResult, formatPostCreatedDate: (String) -> String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(11.dp))
        AsyncImage(
            model = postInfo.profileImg,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(45.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = postInfo.writer,
            style = AppTypography.B1_16,
            color = AppColors.black2,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatPostCreatedDate(postInfo.createdAt),
            style = AppTypography.LB1_13,
            color = AppColors.grey3
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostPhotos(imgs: List<String>, pagerState: PagerState) {
    Box(
        modifier = Modifier
            .height(168.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            AsyncImage(
                model = imgs[page],
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) AppColors.purple2 else AppColors.grey6
                Box(
                    modifier = Modifier
                        .padding(horizontal = (3.5).dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(7.dp)
                )
            }
        }
    }
}

@Composable
fun PostContent(
    postInfo: GetPostDetailResult,
    logics: PostLogics,
    popupUiState: PopupUiState,
    navigateToModify: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .heightIn(183.dp)
                    .padding(top = 11.dp, end = 10.dp)
                    .weight(1f),
                text = postInfo.content,
                style = AppTypography.B2_14,
                color = AppColors.black2
            )
            Column(
                modifier = Modifier.padding(top = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_three_dots_row),
                        tint = AppColors.deepPurple1,
                        contentDescription = null,
                        modifier = Modifier.noRippleClickable {
                            expanded = true
                        }
                    )
                    val deletePostPopupLabel = stringResource(R.string.post_detail_delete_post_pop_up_label)

                    PostDetailDropdownMenu(
                        items = listOf(
                            Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_modify)) {
                                navigateToModify()
                            },
                            Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_report)) {
                                popupUiState.popupStatusLogics.showReportPopup(true) {
                                    popupUiState.popupStatusLogics.showReportPopup(false, {})
                                }
                            },
                            Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_delete)) {
                                popupUiState.popupStatusLogics.showExecutePopup(true, deletePostPopupLabel) {
                                    logics.deletePost(postInfo.postId)
                                    popupUiState.popupStatusLogics.showDeleteCompletePopup(true)
                                    popupUiState.popupStatusLogics.showExecutePopup(false, "") {}
                                }
                            },
                        ),
                        expanded = expanded
                    ) { expanded = it }
                }

                Spacer(modifier = Modifier.height(6.dp))

                var lovedState by remember {
                    mutableStateOf(postInfo.loved)
                }
                var countLoveState by remember {
                    mutableIntStateOf(postInfo.countLove)
                }

                Icon(
                    imageVector =
                    if (!lovedState) ImageVector.vectorResource(R.drawable.ic_heart_empty)
                    else ImageVector.vectorResource(R.drawable.ic_heart_filled),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.noRippleClickable {
                        if (lovedState) {
                            logics.deletePostLoves(postInfo.postId)
                            countLoveState -= 1
                        } else {
                            logics.postPostLoves(postInfo.postId)
                            countLoveState += 1
                        }
                        lovedState = !lovedState
                    }
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = countLoveState.toString(),
                    style = AppTypography.LB2_11,
                    color = AppColors.black2
                )
            }
        }
    }
}

@Composable
fun CommentTextField(
    commentsCount: Int,
    postId: Long,
    postComment: (Long, String) -> Unit,
    postCommentUiState: PostCommentUiState,
    getPostLovesUiState: GetPostLovesUiState,
    context: Context
) {
    var showLoveListPopUp by remember {
        mutableStateOf(false)
    }
    Column {
        Row(
            modifier = Modifier.padding(
                top = 14.dp,
                start = 16.dp,
                end = 12.dp
            )
        ) {
            Text(
                text = "댓글 ${commentsCount}개",
                style = AppTypography.B2_14,
                color = AppColors.grey2,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.post_detail_screen_show_heart),
                style = AppTypography.B2_14,
                color = AppColors.grey2,
                modifier = Modifier.noRippleClickable {
                    if (getPostLovesUiState.isSuccess == false) {
                        showToastMessage(context, getPostLovesUiState.message)
                    } else {
                        showLoveListPopUp = true
                    }
                }
            )
        }
        if (showLoveListPopUp) {
            LoveListPopUp(getPostLovesUiState.result) { showLoveListPopUp = false }
        }
        Spacer(modifier = Modifier.height(10.dp))
        var comments by remember {
            mutableStateOf(TextFieldValue())
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(start = 10.dp, end = 12.dp)
                .background(
                    color = AppColors.grey4,
                    shape = RoundedCornerShape(size = 8.dp)
                )
        ) {
            Row(modifier = Modifier.fillMaxHeight()) {
                BasicTextField(
                    value = comments,
                    onValueChange = {
                        if (it.text.length <= 50) comments = it
                    },
                    textStyle = AppTypography.LB2_11.copy(AppColors.black1),
                    modifier = Modifier
                        .weight(1f)
                        .padding(11.dp)
                ) { innerTextField ->
                    if (comments.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.post_detail_screen_comment_text_field_hint),
                            color = AppColors.grey3,
                            style = AppTypography.LB2_11
                        )
                    }
                    innerTextField()
                }

                if (postCommentUiState.isSuccess == true) {
                    comments = TextFieldValue()
                } else if (postCommentUiState.isSuccess == false) {
                    showToastMessage(context, postCommentUiState.message ?: "댓글 전송에 실패했습니다.")
                }
                Button(
                    onClick = {
                        postComment(postId, comments.text)
                    },
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .height(52.dp)
                        .width(42.dp)
                        .align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.purple1),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(
                        text = stringResource(R.string.post_detail_screen_comment_post_button),
                        style = AppTypography.BTN6_13,
                        color = AppColors.grey6
                    )
                }
            }
        }
    }
}

@Composable
fun CommentItems(
    comments: List<GetCommentsResult>,
    logics: CommentLogics,
    formatCommentCreatedDate: (String) -> String,
    popupUiState: PopupUiState
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        comments.forEach {
            CommentItem(
                it,
                logics,
                formatCommentCreatedDate,
                popupUiState
            )
        }
    }

}

@Composable
fun CommentItem(
    comment: GetCommentsResult,
    logics: CommentLogics,
    formatCommentCreatedDate: (String) -> String,
    popupUiState: PopupUiState
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 12.dp)
            .background(
                color = AppColors.grey4,
                shape = RoundedCornerShape(size = 8.dp)
            )
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(modifier = Modifier.padding(start = 11.dp, top = 10.dp)) {
                AsyncImage(
                    model = comment.profileImg,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 11.dp)
                        .clip(CircleShape)
                        .size(24.dp)
                )
                Text(
                    text = comment.nickname,
                    style = AppTypography.BTN6_13,
                    color = AppColors.black2
                )
            }
            Text(
                modifier = Modifier.padding(start = 11.dp, bottom = 14.58.dp, top = 9.73.dp),
                text = comment.content,
                style = AppTypography.LB2_11,
                color = AppColors.black2
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 3.dp, end = 6.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.align(Alignment.End)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_three_dots_row),
                    contentDescription = null,
                    modifier = Modifier.noRippleClickable { expanded = true },
                    tint = Color.Unspecified,
                )
                val deleteCommentPopupLabel = stringResource(R.string.post_detail_pop_up_delete_comment_label)
                PostDetailDropdownMenu(
                    items = listOf(
                        Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_report)) {
                            popupUiState.popupStatusLogics.showReportPopup(true) {}
                        },
                        Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_delete)) {
                            popupUiState.popupStatusLogics.showExecutePopup(true, deleteCommentPopupLabel) {
                                logics.deleteComment(comment.commentId)
                                popupUiState.popupStatusLogics.showExecutePopup(false, "") {}
                            }
                        },
                    ),
                    expanded = expanded
                ) { expanded = it }
            }

            var commentLikeState by remember {
                mutableStateOf(comment.heart)
            }

            Icon(
                imageVector =
                if (!commentLikeState) ImageVector.vectorResource(R.drawable.ic_heart_empty)
                else ImageVector.vectorResource(R.drawable.ic_heart_filled),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.End)
                    .noRippleClickable {
                        if (commentLikeState) {
                            logics.deleteCommentLoves(comment.commentId)
                        } else {
                            logics.postCommentLoves(comment.commentId)
                        }
                        commentLikeState = !commentLikeState
                    }
            )
            Text(
                text = formatCommentCreatedDate(comment.createdAt),
                style = AppTypography.LB2_11,
                color = AppColors.grey3,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

    }
}

@Composable
fun PostDetailDropdownMenu(
    items: List<Pair<String, () -> Unit>>,
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) },
        modifier = modifier.then(
            Modifier
                .width(112.dp)
                .background(color = AppColors.deepPurple3, shape = RoundedCornerShape(5.dp))
        )
    )
    {
        items.forEach {
            DropdownMenuItem(
                text = {
                    Text(
                        text = it.first,
                        style = AppTypography.LB2_11,
                        color = AppColors.grey6
                    )
                },
                onClick = {
                    onExpandedChange(false)
                    it.second()
                },
                modifier = Modifier.height(35.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailPreview() {
    FamilyMomentsTheme {
        PostDetailScreen(hiltViewModel(), 0, modifier = Modifier, {}) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailDropdownMenuPreview() {
    FamilyMomentsTheme {
        PostDetailDropdownMenu(items = listOf(), modifier = Modifier, true) {}
    }
}
