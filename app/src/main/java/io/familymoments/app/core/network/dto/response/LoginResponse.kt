package io.familymoments.app.core.network.dto.response

import androidx.compose.runtime.Immutable

@Immutable
data class LoginResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: LoginResult = LoginResult(),
)

@Immutable
data class LoginResult(
    val familyId: Long? = null,
    val email: String = "",
    val name: String = "",
    val nickname: String = "",
    val strBirthDate: String = "",
)
