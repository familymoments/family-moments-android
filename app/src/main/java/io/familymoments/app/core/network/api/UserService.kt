package io.familymoments.app.core.network.api

import io.familymoments.app.core.network.dto.request.LoginRequest
import io.familymoments.app.core.network.dto.request.ModifyPasswordRequest
import io.familymoments.app.core.network.dto.request.ProfileEditRequest
import io.familymoments.app.core.network.dto.response.LoginResponse
import io.familymoments.app.core.network.dto.response.LogoutResponse
import io.familymoments.app.core.network.dto.response.ModifyPasswordResponse
import io.familymoments.app.core.network.dto.response.ProfileEditResponse
import io.familymoments.app.core.network.dto.response.SearchMemberResponse
import io.familymoments.app.core.network.dto.response.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {
    @POST("/users/log-in")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest,
        @Header("FCM-Token") fcmToken: String
    ): Response<LoginResponse>

    @POST("/users/reissue")
    suspend fun reissueAccessToken(): Response<Void>

    @GET("/users/profile")
    suspend fun loadUserProfile(
        @Query("familyId") familyId: Long? = null
    ): Response<UserProfileResponse>

    @PATCH("/users/modify-pwd")
    suspend fun modifyPassword(
        @Body modifyPasswordRequest: ModifyPasswordRequest
    ): Response<ModifyPasswordResponse>

    @POST("/users/log-out")
    suspend fun logoutUser(): Response<LogoutResponse>

    @GET("/users")
    suspend fun searchMember(
        @Query("keyword") keyword: String,
        @Query("familyId") familyId: Long? = null
    ): Response<SearchMemberResponse>

    @Multipart
    @POST("/users/edit")
    suspend fun editUserProfile(
        @Part("PatchProfileReqRes") profileEditRequest: ProfileEditRequest,
        @Part profileImg: MultipartBody.Part
    ): Response<ProfileEditResponse>
}
