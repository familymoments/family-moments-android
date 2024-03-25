package io.familymoments.app.feature.postdetail.model.response

data class DeleteCommentResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: String = ""
)
