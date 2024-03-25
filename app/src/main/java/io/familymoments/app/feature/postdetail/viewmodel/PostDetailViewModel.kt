package io.familymoments.app.feature.postdetail.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.CommentRepository
import io.familymoments.app.core.network.repository.PostRepository
import io.familymoments.app.feature.postdetail.model.uistate.CommentLogics
import io.familymoments.app.feature.postdetail.model.uistate.CommentUiState
import io.familymoments.app.feature.postdetail.model.uistate.ExecutePopupUiState
import io.familymoments.app.feature.postdetail.model.uistate.PopupStatusLogics
import io.familymoments.app.feature.postdetail.model.uistate.PopupUiState
import io.familymoments.app.feature.postdetail.model.uistate.PostLogics
import io.familymoments.app.feature.postdetail.model.uistate.PostUiState
import io.familymoments.app.feature.postdetail.model.uistate.ReportPopupUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) : BaseViewModel() {

    private val _postUiState: MutableStateFlow<PostUiState> =
        MutableStateFlow(
            PostUiState(
                logics = PostLogics(
                    getPostDetail = this::getPostDetail,
                    getPostLoves = this::getPostLoves,
                    postPostLoves = this::postPostLoves,
                    deletePostLoves = this::deletePostLoves,
                    deletePost = this::deletePost
                )
            )
        )
    val postUiState: StateFlow<PostUiState> = _postUiState.asStateFlow()

    private val _commentUiState: MutableStateFlow<CommentUiState> =
        MutableStateFlow(
            CommentUiState(
                logics = CommentLogics(
                    getComments = this::getPostComments,
                    postComment = this::postComment,
                    deleteComment = this::deleteComment,
                    postCommentLoves = this::postCommentLoves,
                    deleteCommentLoves = this::deleteCommentLoves
                )
            )
        )
    val commentUiState: StateFlow<CommentUiState> =
        _commentUiState.asStateFlow()

    private val _popupUiState: MutableStateFlow<PopupUiState> = MutableStateFlow(
        PopupUiState(
            popupStatusLogics = PopupStatusLogics(
                this::showDeleteCompletePopup,
                this::showExecutePopup,
                this::showReportPopup
            )
        )
    )
    val popupUiState: StateFlow<PopupUiState> = _popupUiState.asStateFlow()

    fun getPostDetail(index: Long) {
        async(
            operation = { postRepository.getPostDetail(index) },
            onSuccess = {
                val getPostUiState = _postUiState.value.getPostDetailUiState
                _postUiState.value = _postUiState.value.copy(
                    getPostDetailUiState = getPostUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.result
                    )
                )
            },
            onFailure = {
                val postDetailUiState = _postUiState.value.getPostDetailUiState
                _postUiState.value = _postUiState.value.copy(
                    getPostDetailUiState = postDetailUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            })

    }

    fun getPostComments(index: Long) {
        async(
            operation = { commentRepository.getPostComments(index) },
            onSuccess = {
                val getCommentsUiState = _commentUiState.value.getCommentsUiState
                _commentUiState.value = _commentUiState.value.copy(
                    getCommentsUiState = getCommentsUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.result
                    )
                )
            },
            onFailure = {
                val getCommentsUiState = _commentUiState.value.getCommentsUiState
                _commentUiState.value = _commentUiState.value.copy(
                    getCommentsUiState = getCommentsUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            }
        )
    }

    fun getPostLoves(index: Long) {
        async(
            operation = { postRepository.getPostLoves(index) },
            onSuccess = {
                val getPostLovesUiState = _postUiState.value.getPostLovesUiState
                _postUiState.value = _postUiState.value.copy(
                    getPostLovesUiState = getPostLovesUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.results
                    )
                )
            },
            onFailure = {
                val getPostLovesUiState = _postUiState.value.getPostLovesUiState
                _postUiState.value = _postUiState.value.copy(
                    getPostLovesUiState = getPostLovesUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            }
        )
    }

    fun postComment(index: Long, content: String) {
        async(
            operation = { commentRepository.postComment(content, index) },
            onSuccess = {
                val postCommentUiState = _commentUiState.value.postCommentUiState
                _commentUiState.value = _commentUiState.value.copy(
                    postCommentUiState = postCommentUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.result
                    )
                )
            },
            onFailure = {
                val postCommentUiState = _commentUiState.value.postCommentUiState
                _commentUiState.value = _commentUiState.value.copy(
                    postCommentUiState = postCommentUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            }
        )
    }

    fun deleteComment(index: Long) {
        async(
            operation = { commentRepository.deleteComment(index) },
            onSuccess = {
                val deleteCommentUiState = _commentUiState.value.deleteCommentUiState
                _commentUiState.value = _commentUiState.value.copy(
                    deleteCommentUiState = deleteCommentUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.result
                    )
                )
            },
            onFailure = {
                val deleteCommentUiState = _commentUiState.value.deleteCommentUiState
                _commentUiState.value = _commentUiState.value.copy(
                    deleteCommentUiState = deleteCommentUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            }
        )
    }

    fun postCommentLoves(commentId: Long) {
        async(
            operation = { commentRepository.postCommentLoves(commentId) },
            onSuccess = {
                val postCommentLovesUiState = _commentUiState.value.postCommentLovesUiState
                _commentUiState.value = _commentUiState.value.copy(
                    postCommentLovesUiState = postCommentLovesUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.result
                    )
                )
            },
            onFailure = {
                val postCommentLovesUiState = _commentUiState.value.postCommentLovesUiState
                _commentUiState.value = _commentUiState.value.copy(
                    postCommentLovesUiState = postCommentLovesUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            }
        )
    }

    fun deleteCommentLoves(commentId: Long) {
        async(
            operation = { commentRepository.deleteCommentLoves(commentId) },
            onSuccess = {
                val deleteCommentLovesUiState = _commentUiState.value.deleteCommentLovesUiState
                _commentUiState.value = _commentUiState.value.copy(
                    deleteCommentLovesUiState = deleteCommentLovesUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.result
                    )
                )
            },
            onFailure = {
                val deleteCommentLovesUiState = _commentUiState.value.deleteCommentLovesUiState
                _commentUiState.value = _commentUiState.value.copy(
                    deleteCommentLovesUiState = deleteCommentLovesUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            }
        )
    }

    fun postPostLoves(index: Long) {
        async(
            operation = { postRepository.postPostLoves(index) },
            onSuccess = {
                val postPostLovesUiState = _postUiState.value.postPostLovesUiState
                _postUiState.value = _postUiState.value.copy(
                    postPostLovesUiState = postPostLovesUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.result
                    )
                )
            },
            onFailure = {
                val postPostLovesUiState = _postUiState.value.postPostLovesUiState
                _postUiState.value = _postUiState.value.copy(
                    postPostLovesUiState = postPostLovesUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            }
        )
    }

    fun deletePostLoves(index: Long) {
        async(
            operation = { postRepository.deletePostLoves(index) },
            onSuccess = {
                val deletePostLovesUiState = _postUiState.value.deletePostLovesUiState
                _postUiState.value = _postUiState.value.copy(
                    deletePostLovesUiState = deletePostLovesUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.result
                    )
                )
            },
            onFailure = {
                val deletePostLovesUiState = _postUiState.value.deletePostLovesUiState
                _postUiState.value = _postUiState.value.copy(
                    deletePostLovesUiState = deletePostLovesUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        message = it.message
                    )
                )
            }
        )
    }

    fun deletePost(index: Long) {
        async(
            operation = { postRepository.deletePost(index) },
            onSuccess = {
                val deletePostUiState = _postUiState.value.deletePostUiState
                _postUiState.value = _postUiState.value.copy(
                    deletePostUiState = deletePostUiState.copy(
                        isSuccess = true,
                        isLoading = isLoading.value,
                        result = it.message
                    )
                )
            },
            onFailure = {
                val deletePostUiState = _postUiState.value.deletePostUiState
                _postUiState.value = _postUiState.value.copy(
                    deletePostUiState = deletePostUiState.copy(
                        isSuccess = false,
                        isLoading = isLoading.value,
                        result = it.message
                    )
                )
            }
        )
    }

    fun showDeleteCompletePopup(status: Boolean) {
        _popupUiState.value = _popupUiState.value.copy(
            showDeleteCompletePopup = status
        )
    }

    fun showExecutePopup(status: Boolean, content: String, execute: () -> Unit) {
        _popupUiState.value = _popupUiState.value.copy(
            executePopupUiState = ExecutePopupUiState(status, content, execute)
        )
    }

    fun showReportPopup(status: Boolean, execute: () -> Unit) {
        _popupUiState.value = _popupUiState.value.copy(
            reportPopupUiState = ReportPopupUiState(status, execute)
        )
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    fun formatPostCreatedDate(createdAt: String): String {
        val date = LocalDate.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val formattedString = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        return "$formattedString (${
            when (date.dayOfWeek) {
                DayOfWeek.MONDAY -> "월"
                DayOfWeek.TUESDAY -> "화"
                DayOfWeek.WEDNESDAY -> "수"
                DayOfWeek.THURSDAY -> "목"
                DayOfWeek.FRIDAY -> "금"
                DayOfWeek.SATURDAY -> "토"
                DayOfWeek.SUNDAY -> "일"
            }
        })"
    }

    fun formatCommentCreatedDate(createdAt: String): String {
        val date = LocalDateTime.parse(createdAt)
        val now = LocalDateTime.now()
        val durationSeconds = Duration.between(date, now).seconds
        return when {
            durationSeconds < 60 -> "${durationSeconds}초 전"
            durationSeconds < 3600 -> "${durationSeconds / 60}분 전"
            durationSeconds < 86400 -> "${durationSeconds / 3600}시간 전"
            durationSeconds < 604800 -> "${durationSeconds / 86400}일 전"
            durationSeconds < 2592000 -> "${durationSeconds / 604800}주 전"
            durationSeconds < 31536000 -> "${durationSeconds / 2592000}달 전"
            else -> "${durationSeconds / 31536000}년 전"
        }
    }
}
