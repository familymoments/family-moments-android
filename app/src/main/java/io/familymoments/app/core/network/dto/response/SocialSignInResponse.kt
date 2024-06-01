package io.familymoments.app.core.network.dto.response

import androidx.compose.runtime.Immutable


@Immutable
data class SocialSignInResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: SocialSignInResult
)

data class SocialSignInResult(
    val isExisted: Boolean,
    val name: String? = "",
    val email: String? = "",
    val strBirthDate: String? = "",
    val nickname: String? = "",
    val picture: String? = "",
    val familyId: Long? = 0
)
