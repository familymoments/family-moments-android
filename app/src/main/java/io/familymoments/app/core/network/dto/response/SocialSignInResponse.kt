package io.familymoments.app.core.network.dto.response

import androidx.compose.runtime.Immutable


@Immutable
data class SocialSignInResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val isExisted: Boolean?, // true면 회원가입으로 이동, false면 로그인 성공
    val snsId: String,
    val name: String,
    val email: String,
    val strBirthDate: String,
    val nickname: String,
    val picture: String,
    val familyId: Long? = 0
)
