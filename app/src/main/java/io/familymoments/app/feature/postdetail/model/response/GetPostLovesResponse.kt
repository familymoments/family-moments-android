package io.familymoments.app.feature.postdetail.model.response

data class GetPostLovesResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val results: List<GetPostLovesResult> = listOf()
)

data class GetPostLovesResult(
    val nickname: String = "",
    val profileImg: String = ""
)
