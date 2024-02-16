package io.familymoments.app.core.network.api

import io.familymoments.app.feature.login.model.request.LoginRequest
import io.familymoments.app.feature.login.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/users/log-in")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @POST("/users/validate")
    suspend fun checkAccessTokenValidation(): Response<Void>
    @POST("/users/reissue")
    suspend fun reissueAccessToken(): Response<Void>
}
