package io.familymoments.app.core.network.dto.request

data class VerifyEmailVerificationCodeRequest(
    val email: String,
    val code:String
)
