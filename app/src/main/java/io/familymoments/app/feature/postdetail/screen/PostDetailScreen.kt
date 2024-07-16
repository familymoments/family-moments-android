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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.component.PostDropdownMenu
import io.familymoments.app.core.component.popup.CompletePopUp
import io.familymoments.app.core.component.popup.DeletePopUp
import io.familymoments.app.core.component.popup.LoveListPopUp
import io.familymoments.app.core.component.popup.ReportPopUp
import io.familymoments.app.core.network.dto.response.GetCommentsResult
import io.familymoments.app.core.network.dto.response.GetPostDetailResult
import io.familymoments.app.core.network.dto.response.GetPostLovesResult
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.util.noRippleClickable
import io.familymoments.app.feature.postdetail.component.postDetailContentShadow
import io.familymoments.app.feature.postdetail.uistate.PostDetailPopupType
import io.familymoments.app.feature.postdetail.uistate.PostDetailUiState
import io.familymoments.app.feature.postdetail.viewmodel.PostDetailViewModel

@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel,
    index: Long,
    modifier: Modifier,
    navigateToBack: () -> Unit,
    navigateToModify: (GetPostDetailResult) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getNickname()
        viewModel.getPostDetail(index)
        viewModel.getComments(index)
        viewModel.getPostLoves(index)
    }
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val popup = uiState.popup
    val postDetail = uiState.postDetail

    LaunchedEffectSetUpData(
        index,
        viewModel::getNickname,
        viewModel::getPostDetail,
        viewModel::getComments,
        viewModel::getPostLoves
    )
    LaunchedEffectShowPopup(
        popup,
        viewModel::deletePost,
        viewModel::deleteComment,
        viewModel::dismissPopup,
        navigateToBack,
    )
    LaunchedEffectShowErrorMessage(uiState, context, viewModel::resetSuccess)
    PostDetailScreenUI(
        modifier = modifier,
        uiState = uiState,
        isPostDetailExist = viewModel.checkPostDetailExist(postDetail),
        formatPostCreatedDate = viewModel::formatPostCreatedDate,
        showDeletePostPopup = viewModel::showDeletePostPopup,
        showReportPostPopup = viewModel::showReportPostPopup,
        navigateToPostModify = navigateToModify,
        postComment = viewModel::postComment,
        showLoveListPopup = viewModel::showLoveListPopup,
        makeCommentAvailable = viewModel::makeCommentAvailable,
        formatCommentCreatedDate = viewModel::formatCommentCreatedDate,
        showReportCommentPopup = viewModel::showReportCommentPopup,
        showDeleteCommentPopup = viewModel::showDeleteCommentPopup,
        onClickPostLoves = {
            if (postDetail.loved) {
                viewModel.deletePostLoves(it)
            } else {
                viewModel.postPostLoves(it)
            }
        },
        onClickCommentLoves = { heart, id ->
            if (heart) {
                viewModel.deleteCommentLoves(id)
            } else {
                viewModel.postCommentLoves(id)
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDetailScreenUI(
    modifier: Modifier = Modifier,
    uiState: PostDetailUiState,
    isPostDetailExist: Boolean = true,
    formatPostCreatedDate: (String) -> String,
    showDeletePostPopup: (Long) -> Unit = {},
    showReportPostPopup: (Long) -> Unit = {},
    navigateToPostModify: (GetPostDetailResult) -> Unit = {},
    postComment: (Long, String) -> Unit = { _, _ -> },
    showLoveListPopup: (List<GetPostLovesResult>) -> Unit = {},
    makeCommentAvailable: () -> Unit = {},
    formatCommentCreatedDate: (String) -> String,
    showReportCommentPopup: (Long) -> Unit = {},
    showDeleteCommentPopup: (Long) -> Unit = {},
    onClickPostLoves: (Long) -> Unit = {},
    onClickCommentLoves: (Boolean, Long) -> Unit = { _, _ -> },
) {

    val pagerState = rememberPagerState(pageCount = { uiState.postDetail.imgs.size })
    LazyColumn {
        item {
            Column(
                modifier = modifier.padding(
                    start = 16.dp, end = 16.dp, top = 26.dp, bottom = 63.dp
                )
            ) {
                if (isPostDetailExist) {
                    WriterInfo(
                        writer = uiState.postDetail.writer,
                        profileImg = uiState.postDetail.profileImg,
                        createdAt = formatPostCreatedDate(uiState.postDetail.createdAt),
                    )
                }
                Box(modifier = Modifier.postDetailContentShadow()) {
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(AppColors.grey6)
                    ) {
                        PostPhotos(uiState.postDetail.imgs, pagerState)
                        PostContent(
                            uiState.userNickname,
                            uiState.postDetail,
                            showDeletePostPopup,
                            showReportPostPopup,
                            onClickPostLoves
                        ) { navigateToPostModify(uiState.postDetail) }

                        CommentTextField(
                            uiState.comments.size,
                            uiState.postDetail.postId,
                            postComment,
                            showLoveListPopup,
                            uiState.postLoves,
                            uiState.resetComment,
                            makeCommentAvailable
                        )
                        if (uiState.comments.isNotEmpty()) {
                            CommentItems(
                                uiState.userNickname,
                                uiState.comments,
                                formatCommentCreatedDate,
                                showReportCommentPopup,
                                showDeleteCommentPopup,
                                onClickCommentLoves
                            )
                        }

                    }
                }

            }
        }
    }
}

@Composable
fun LaunchedEffectSetUpData(
    index: Long,
    getNickname: () -> Unit,
    getPostDetail: (Long) -> Unit,
    getComments: (Long) -> Unit,
    getPostLoves: (Long) -> Unit
) {
    LaunchedEffect(Unit) {
        getNickname()
        getPostDetail(index)
        getComments(index)
        getPostLoves(index)
    }
}

@Composable
fun LaunchedEffectShowPopup(
    popup: PostDetailPopupType?,
    deletePost: (Long) -> Unit,
    deleteComment: (Long) -> Unit,
    dismissPopup: () -> Unit,
    navigateToBack: () -> Unit,
) {
    val showPopup = remember { mutableStateOf(false) }
    LaunchedEffect(popup) {
        showPopup.value = popup != null
    }
    if (showPopup.value) {
        when (popup) {
            is PostDetailPopupType.DeleteComment -> {
                DeletePopUp(
                    content = stringResource(id = R.string.post_detail_pop_up_delete_comment_label),
                    delete = { deleteComment(popup.commentId) },
                    onDismissRequest = dismissPopup
                )
            }

            PostDetailPopupType.DeleteCommentFailed -> {
                //todo: 댓글 삭제 실패 팝업
            }

            PostDetailPopupType.DeleteCommentSuccess -> {
                CompletePopUp(
                    content = stringResource(id = R.string.post_detail_delete_complete_pop_label),
                    onDismissRequest = {
                        dismissPopup()
                    }
                )
            }

            is PostDetailPopupType.DeletePost -> {
                DeletePopUp(
                    content = stringResource(id = R.string.post_detail_delete_post_pop_up_label),
                    delete = { deletePost(popup.postId) },
                    onDismissRequest = dismissPopup
                )
            }

            PostDetailPopupType.DeletePostFailed -> {
                //todo: 글 삭제 실패 팝업
            }

            PostDetailPopupType.DeletePostSuccess -> {
                CompletePopUp(
                    content = stringResource(id = R.string.post_detail_delete_complete_pop_label),
                    onDismissRequest = {
                        dismissPopup()
                        navigateToBack()
                    }
                )
            }

            is PostDetailPopupType.ReportComment -> {
                ReportPopUp(
                    onDismissRequest = dismissPopup,
                    onReportRequest = {
                        //todo: 댓글 신고하기 기능 구현
                    }
                )
            }

            PostDetailPopupType.ReportCommentFailed -> {
                //todo: 댓글 신고 실패 팝업
            }

            PostDetailPopupType.ReportCommentSuccess -> {
                //todo: 댓글 신고 성공 팝업
            }

            is PostDetailPopupType.ReportPost -> {
                ReportPopUp(
                    onDismissRequest = dismissPopup,
                    onReportRequest = {
                        //todo: 글 신고하기 기능 구현
                    }
                )
            }

            PostDetailPopupType.ReportPostFailed -> {
                //todo: 글 신고 실패 팝업
            }

            PostDetailPopupType.ReportPostSuccess -> {
                //todo: 글 신고 성공 팝업
            }

            is PostDetailPopupType.LoveList -> {
                LoveListPopUp(
                    postLoves = popup.loves,
                    onDismissRequest = dismissPopup
                )
            }

            null -> {}
        }
    }
}

@Composable
fun LaunchedEffectShowErrorMessage(
    uiState: PostDetailUiState,
    context: Context,
    resetSuccess: () -> Unit
) {
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess == false) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
            resetSuccess()
        }
    }
}

@Composable
fun WriterInfo(
    writer: String,
    profileImg: String,
    createdAt: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(11.dp))
        AsyncImage(
            model = profileImg,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(45.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = writer,
            style = AppTypography.B1_16,
            color = AppColors.black2,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = createdAt,
            style = AppTypography.LB1_13,
            color = AppColors.grey3
        )
    }
    Spacer(modifier = Modifier.height(15.dp))
    HorizontalDivider(Modifier.height(1.dp), color = AppColors.grey7)
    Spacer(modifier = Modifier.height(19.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostPhotos(imgs: List<String>, pagerState: PagerState) {
    Box(
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                model = imgs[page],
                contentDescription = null
            )
        }
        if (imgs.size > 1) {
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
}

@Composable
fun PostContent(
    userNickname: String,
    postInfo: GetPostDetailResult,
    showDeletePostPopup: (Long) -> Unit,
    showReportPostPopup: (Long) -> Unit,
    onClickPostLoves: (Long) -> Unit,
    navigateToModify: () -> Unit,
) {
    var menuExpanded by remember {
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
                        tint = AppColors.grey8,
                        contentDescription = null,
                        modifier = Modifier.noRippleClickable {
                            menuExpanded = true
                        }
                    )
                    PostDropdownMenu(
                        items =
                        if (userNickname == postInfo.writer) listOf(
                            Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_modify)) {
                                navigateToModify()
                            },
                            Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_report)) {
                                showReportPostPopup(postInfo.postId)
                            },
                            Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_delete)) {
                                showDeletePostPopup(postInfo.postId)
                            },
                        ) else listOf(
                            Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_report)) {
                                showReportPostPopup(postInfo.postId)
                            },
                        ),
                        expanded = menuExpanded
                    ) { menuExpanded = it }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Icon(
                    imageVector =
                    if (!postInfo.loved) ImageVector.vectorResource(R.drawable.ic_heart_empty)
                    else ImageVector.vectorResource(R.drawable.ic_heart_filled),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.noRippleClickable {
                        onClickPostLoves(postInfo.postId)
                    }
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = postInfo.countLove.toString(),
                    style = AppTypography.LB2_11,
                    color = AppColors.black2
                )
            }
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
            .background(color = AppColors.grey4)
    )
}

@Composable
fun CommentTextField(
    commentsCount: Int,
    postId: Long,
    postComment: (Long, String) -> Unit,
    showLoveListPopup: (List<GetPostLovesResult>) -> Unit,
    postLoves: List<GetPostLovesResult>,
    resetComment: Boolean,
    makeCommentAvailable: () -> Unit
) {
    var comment by remember {
        mutableStateOf(TextFieldValue())
    }

    if (resetComment) {
        comment = TextFieldValue()
        makeCommentAvailable()
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
                    showLoveListPopup(postLoves)
                }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
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
            val focusRequester = remember { FocusRequester() }

            Row(modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .background(AppColors.grey4)) {
                BasicTextField(
                    value = comment,
                    onValueChange = {
                        if (it.text.length <= 50) comment = it
                    },
                    textStyle = AppTypography.LB2_11.copy(AppColors.black1),
                    modifier = Modifier
                        .weight(1f)
                        .padding(11.dp)
                        .focusRequester(focusRequester)
                ) { innerTextField ->
                    if (comment.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.post_detail_screen_comment_text_field_hint),
                            color = AppColors.grey3,
                            style = AppTypography.LB2_11
                        )
                    }
                    innerTextField()
                }

                Button(
                    onClick = {
                        postComment(postId, comment.text)
                    },
                    enabled = comment.text.trim().isNotEmpty(),
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .height(52.dp)
                        .width(42.dp)
                        .align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.grey8,
                        disabledContainerColor = AppColors.grey7
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(10.dp)
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
    Spacer(modifier = Modifier.height(18.dp))
}

@Composable
fun CommentItems(
    userNickname: String,
    comments: List<GetCommentsResult>,
    formatCommentCreatedDate: (String) -> String,
    showReportCommentPopup: (Long) -> Unit,
    showDeleteCommentPopup: (Long) -> Unit,
    onClickCommentLoves: (Boolean, Long) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        comments.forEach {
            CommentItem(
                userNickname,
                it,
                formatCommentCreatedDate,
                showReportCommentPopup,
                showDeleteCommentPopup,
                onClickCommentLoves
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun CommentItem(
    userNickname: String,
    comment: GetCommentsResult,
    formatCommentCreatedDate: (String) -> String,
    showReportCommentPopup: (Long) -> Unit,
    showDeleteCommentPopup: (Long) -> Unit,
    onClickCommentLoves: (Boolean, Long) -> Unit
) {
    var menuExpanded by remember {
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
                    modifier = Modifier.noRippleClickable { menuExpanded = true },
                    tint = Color.Unspecified,
                )
                PostDropdownMenu(
                    items =
                    if (userNickname == comment.nickname) listOf(
                        Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_report)) {
                            showReportCommentPopup(comment.commentId)
                        },
                        Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_delete)) {
                            showDeleteCommentPopup(comment.commentId)
                        },
                    ) else listOf(
                        Pair(stringResource(id = R.string.post_detail_screen_drop_down_menu_report)) {
                            showReportCommentPopup(comment.commentId)
                        },
                    ),
                    expanded = menuExpanded
                ) { menuExpanded = it }
            }

            Icon(
                imageVector =
                if (!comment.heart) ImageVector.vectorResource(R.drawable.ic_heart_empty)
                else ImageVector.vectorResource(R.drawable.ic_heart_filled),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.End)
                    .noRippleClickable {
                        onClickCommentLoves(comment.heart, comment.commentId)
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

@Preview(showBackground = true)
@Composable
fun PostDetailScreenUIPreview() {
    PostDetailScreenUI(
        uiState = PostDetailUiState(
            postDetail = GetPostDetailResult(
                writer = "nickname",
                content = "content"
            ),
            comments = List(10) {
                GetCommentsResult(
                    commentId = it.toLong(),
                    nickname = "nickname$it",
                    content = "content$it"
                )
            }
        ),
        formatPostCreatedDate = { "2024-04-29" },
        formatCommentCreatedDate = { "방금" },
    )
}

@Preview(showBackground = true)
@Composable
fun PostDetailDropdownMenuPreview() {
    PostDropdownMenu(items = listOf(), modifier = Modifier, true) {}
}
