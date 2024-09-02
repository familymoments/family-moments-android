package io.familymoments.app.core.network.api

import io.familymoments.app.core.network.dto.request.CheckEmailRequest
import io.familymoments.app.core.network.dto.request.CheckIdRequest
import io.familymoments.app.core.network.dto.request.SendEmailVerificationCodeRequest
import io.familymoments.app.core.network.dto.request.SignUpRequest
import io.familymoments.app.core.network.dto.request.UserJoinReq
import io.familymoments.app.core.network.dto.request.VerifyEmailVerificationCodeRequest
import io.familymoments.app.core.network.dto.response.ApiResponse
import io.familymoments.app.core.network.dto.response.CheckEmailResponse
import io.familymoments.app.core.network.dto.response.CheckIdResponse
import io.familymoments.app.core.network.dto.response.SignUpResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SignInService {
    @POST("/users/check-id")
    suspend fun checkId(@Body checkIdRequest: CheckIdRequest): CheckIdResponse

    @POST("/users/check-email")
    suspend fun checkEmail(@Body checkEmailRequest: CheckEmailRequest): CheckEmailResponse

    @Multipart
    @POST("/users/sign-up")
    suspend fun executeSignUp(
        @Part profileImg: MultipartBody.Part,
        @Part("newUser") signUpRequest: SignUpRequest
    ): SignUpResponse

    @Multipart
    @POST("/users/oauth2/social/join")
    suspend fun executeSocialSignUp(
        @Part profileImg: MultipartBody.Part,
        @Part("userJoinReq") userJoinReq: UserJoinReq
    ): SignUpResponse

    @POST("/users/send-email")
    suspend fun sendEmailVerificationCode(
        @Body sendEmailVerificationCodeRequest: SendEmailVerificationCodeRequest
    ): Response<ApiResponse<String>>

    @POST("/users/verify-email")
    suspend fun verifyEmailVerificationCode(
        @Body verifyEmailVerificationCodeRequest: VerifyEmailVerificationCodeRequest
    ): Response<ApiResponse<String>>
}
