package io.familymoments.app.core.network.api

import io.familymoments.app.core.network.model.UserProfileResponse
import io.familymoments.app.feature.login.model.request.LoginRequest
import io.familymoments.app.feature.login.model.response.LoginResponse
import io.familymoments.app.feature.profile.model.request.ProfileEditRequest
import io.familymoments.app.feature.profile.model.response.ProfileEditResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {
    @POST("/users/log-in")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @POST("/users/reissue")
    suspend fun reissueAccessToken(): Response<Void>
    @GET("/users/profile")
    suspend fun loadUserProfile(
        @Query("familyId") familyId:Long? = null
    ): Response<UserProfileResponse>

    @Multipart
    @POST("/users/edit")
    suspend fun editUserProfile(
        @Part("PatchProfileReqRes") profileEditRequest: ProfileEditRequest,
        @Part profileImg: MultipartBody.Part
    ): Response<ProfileEditResponse>
}
