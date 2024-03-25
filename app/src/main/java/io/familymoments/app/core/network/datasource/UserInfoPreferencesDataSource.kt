package io.familymoments.app.core.network.datasource

import io.familymoments.app.core.network.model.UserProfile
import io.familymoments.app.feature.profile.model.response.ProfileEditResult

interface UserInfoPreferencesDataSource {
    suspend fun saveAccessToken(token: String)
    suspend fun loadAccessToken(): String
    suspend fun saveFamilyId(familyId:Long)
    suspend fun loadFamilyId():Long
    suspend fun saveUserProfile(userProfile: UserProfile)
    suspend fun loadUserProfile():UserProfile
    suspend fun resetPreferencesData()
    suspend fun updateUserProfile(profileEditResult: ProfileEditResult)
}
