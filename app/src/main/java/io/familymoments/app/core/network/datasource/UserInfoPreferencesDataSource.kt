package io.familymoments.app.core.network.datasource

import io.familymoments.app.core.network.dto.response.ProfileEditResult
import io.familymoments.app.core.network.dto.response.UserProfile

interface UserInfoPreferencesDataSource {
    suspend fun saveAccessToken(token: String)
    suspend fun loadAccessToken(): String
    suspend fun saveFCMToken(token: String)
    suspend fun loadFCMToken(): String
    suspend fun saveFamilyId(familyId: Long)
    suspend fun loadFamilyId(): Long
    suspend fun saveUserProfile(userProfile: UserProfile)
    suspend fun loadUserProfile(): UserProfile
    suspend fun loadUserProfileImg(): String

    fun resetPreferencesData()
    fun removeFamilyId()
    suspend fun updateUserProfile(profileEditResult: ProfileEditResult)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun loadRefreshToken(): String

    fun saveLoginType(loginType: String)
    fun loadLoginType(): String
}
