package io.familymoments.app.core.network.dto.response

import androidx.compose.runtime.Immutable

@Immutable
data class GetPostLovesResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: List<GetPostLovesResult> = listOf()
)

data class GetPostLovesResult(
    val nickname: String = "",
    val profileImg: String = ""
)
