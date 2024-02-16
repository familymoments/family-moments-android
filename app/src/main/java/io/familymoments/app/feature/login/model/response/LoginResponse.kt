package io.familymoments.app.feature.login.model.response


data class LoginResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val loginResult: LoginResult = LoginResult(),
)

data class LoginResult(val familyId: Long = 0)
