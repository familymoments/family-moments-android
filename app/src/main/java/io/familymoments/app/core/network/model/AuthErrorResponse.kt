package io.familymoments.app.core.network.model

sealed class AuthErrorResponse:Throwable() {
    data object RefreshTokenExpiration: AuthErrorResponse()
    data class CommonError(override val message:String): AuthErrorResponse()
}
