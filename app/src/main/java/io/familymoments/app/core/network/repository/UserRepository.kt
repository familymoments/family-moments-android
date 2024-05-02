package io.familymoments.app.core.network.repository

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.dto.request.ModifyPasswordRequest
import io.familymoments.app.core.network.dto.request.ProfileEditRequest
import io.familymoments.app.core.network.dto.response.LoginResponse
import io.familymoments.app.core.network.dto.response.LogoutResponse
import io.familymoments.app.core.network.dto.response.ModifyPasswordResponse
import io.familymoments.app.core.network.dto.response.ProfileEditResponse
import io.familymoments.app.core.network.dto.response.SearchMemberResponse
import io.familymoments.app.core.network.dto.response.UserProfileResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

// User API 중 토큰이 필요한 API 들
interface UserRepository {
    suspend fun loginUser(
        username: String,
        password: String,
        fcmToken: String
    ): Flow<Resource<LoginResponse>>

    suspend fun reissueAccessToken(): Flow<Resource<Unit>>
    suspend fun loadUserProfile(familyId: Long?): Flow<Resource<UserProfileResponse>>
    suspend fun modifyPassword(modifyPasswordRequest: ModifyPasswordRequest): Flow<Resource<ModifyPasswordResponse>>
    suspend fun logoutUser(): Flow<Resource<LogoutResponse>>
    suspend fun searchMember(
        keyword: String,
        newFamily: Boolean
    ): Flow<Resource<SearchMemberResponse>>

    suspend fun editUserProfile(
        profileEditRequest: ProfileEditRequest,
        profileImg: MultipartBody.Part
    ): Flow<Resource<ProfileEditResponse>>
}
