package io.familymoments.app.feature.postdetail.model.response

data class DeletePostResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = ""
)
