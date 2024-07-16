package io.familymoments.app.feature.postdetail.uistate

import io.familymoments.app.core.network.dto.response.GetCommentsResult
import io.familymoments.app.core.network.dto.response.GetPostDetailResult
import io.familymoments.app.core.network.dto.response.GetPostLovesResult

data class PostDetailUiState(
    val isSuccess: Boolean? = null,
    val errorMessage: String? = null,
    val postDetail:GetPostDetailResult = GetPostDetailResult(),
    val comments:List<GetCommentsResult> = listOf(),
    val postLoves: List<GetPostLovesResult> = listOf(),
    val resetComment:Boolean = false,
    val popup: PostDetailPopupType? = null,
    val userNickname:String = ""
)

sealed interface PostDetailPopupType {
    data class DeletePost(val postId: Long) : PostDetailPopupType
    data object DeletePostSuccess : PostDetailPopupType
    data object DeletePostFailed : PostDetailPopupType
    data class DeleteComment(val commentId: Long) : PostDetailPopupType
    data object DeleteCommentSuccess : PostDetailPopupType
    data object DeleteCommentFailed : PostDetailPopupType
    data class ReportPost(val postId: Long) : PostDetailPopupType
    data object ReportPostSuccess : PostDetailPopupType
    data class ReportPostFailed(val message:String) : PostDetailPopupType
    data class ReportComment(val commentId: Long) : PostDetailPopupType
    data object ReportCommentSuccess : PostDetailPopupType
    data class ReportCommentFailed(val message: String) : PostDetailPopupType
    data class LoveList(val loves: List<GetPostLovesResult>) : PostDetailPopupType
}
