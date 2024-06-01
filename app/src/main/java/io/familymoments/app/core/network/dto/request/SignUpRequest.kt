package io.familymoments.app.core.network.dto.request

data class SignUpRequest(
    val id: String,
    val password: String,
    val name: String,
    val email: String,
    val strBirthDate: String,
    val nickname: String
)

data class UserJoinReq(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val strBirthDate: String = "",
    val nickname: String = "",
    val userType: String = ""
)
