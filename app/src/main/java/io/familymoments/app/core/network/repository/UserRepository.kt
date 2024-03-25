package io.familymoments.app.core.network.repository

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.model.UserProfileResponse
import io.familymoments.app.feature.creatingfamily.model.response.SearchMemberResponse
import io.familymoments.app.feature.login.model.response.LoginResponse
import io.familymoments.app.feature.profile.model.request.ProfileEditRequest
import io.familymoments.app.feature.profile.model.response.ProfileEditResponse
import io.familymoments.app.feature.modifypassword.model.request.ModifyPasswordRequest
import io.familymoments.app.feature.modifypassword.model.response.ModifyPasswordResponse
import io.familymoments.app.feature.mypage.model.response.LogoutResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

// User API 중 토큰이 필요한 API 들
interface UserRepository {
    suspend fun loginUser(username: String, password: String): Flow<Resource<LoginResponse>>
    suspend fun reissueAccessToken(): Flow<Resource<Unit>>
    suspend fun loadUserProfile(familyId:Long?):Flow<Resource<UserProfileResponse>>

    suspend fun modifyPassword(modifyPasswordRequest: ModifyPasswordRequest): Flow<Resource<ModifyPasswordResponse>>

    suspend fun logoutUser(): Flow<Resource<LogoutResponse>>
    suspend fun searchMember(keyword:String, newFamily: Boolean):Flow<Resource<SearchMemberResponse>>

    suspend fun editProfile(
        profileEditRequest: ProfileEditRequest,
        profileImg: MultipartBody.Part
    ): Flow<Resource<ProfileEditResponse>>
}
