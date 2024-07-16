package io.familymoments.app.core.network.dto.response

import androidx.compose.runtime.Immutable

@Immutable
data class SearchMemberResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: List<Member> = listOf()
)

data class Member(
    val id: String = "",
    val profileImg: String = "",
    val status: Int = 0,
    val nickname: String = ""
)
