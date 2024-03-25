package io.familymoments.app.feature.postdetail.model.response

data class GetCommentsIndexResponse(
    val isSuccess: Boolean = false,
    val code: Int = -1,
    val message: String = "",
    val result: List<GetCommentsResult> = listOf()
)

data class GetCommentsResult(
    val commentId: Long = -1,
    val nickname: String = "",
    val profileImg: String = "",
    val content: String = "",
    val heart: Boolean = false,
    val createdAt: String = ""
)
