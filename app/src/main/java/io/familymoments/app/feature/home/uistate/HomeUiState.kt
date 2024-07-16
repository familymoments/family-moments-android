package io.familymoments.app.feature.home.uistate

import androidx.compose.runtime.Immutable
import io.familymoments.app.core.network.HttpResponseMessage.NO_POST_404
import io.familymoments.app.core.network.dto.response.Post

@Immutable
data class HomeUiState(
    val isSuccess: Boolean? = null,
    val isLoading: Boolean? = null,
    val errorMessage: String? = null,
    val userNickname: String = "",
    val dday: String = "",
    val posts: List<Post> = emptyList(),
    val popup: PostPopupType? = null
) {
    val hasNoPost = errorMessage == NO_POST_404
}

sealed interface PostPopupType {
    data object PostLovesFailure : PostPopupType
    data object DeleteLovesFailure : PostPopupType
    data class DeletePost(val postId: Long) : PostPopupType
    data object DeletePostSuccess : PostPopupType
    data object DeletePostFailure : PostPopupType
    data class ReportPost(val postId: Long) : PostPopupType
    data object ReportPostSuccess : PostPopupType
    data class ReportPostFailure(val message:String) : PostPopupType
}
