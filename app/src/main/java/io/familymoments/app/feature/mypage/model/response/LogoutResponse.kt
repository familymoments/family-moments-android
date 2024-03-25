package io.familymoments.app.feature.mypage.model.response

data class LogoutResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = ""
)
