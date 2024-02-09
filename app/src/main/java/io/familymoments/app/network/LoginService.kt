package io.familymoments.app.network

import io.familymoments.app.model.LoginRequest
import io.familymoments.app.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/users/log-in")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @POST("/users/validate")
    suspend fun checkValidation(): Response<Void>
}
