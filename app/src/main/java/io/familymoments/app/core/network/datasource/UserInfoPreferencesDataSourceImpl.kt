package io.familymoments.app.core.network.datasource

import android.content.SharedPreferences
import io.familymoments.app.core.network.model.UserProfile
import io.familymoments.app.feature.profile.model.response.ProfileEditResult
import javax.inject.Inject

class UserInfoPreferencesDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserInfoPreferencesDataSource {
    override suspend fun saveAccessToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(ACCESS_TOKEN_KEY, token)
            apply()
        }
    }

    override suspend fun loadAccessToken(): String {
        return sharedPreferences.getString(
            ACCESS_TOKEN_KEY,
            DEFAULT_TOKEN_VALUE
        ) ?: throw IllegalStateException(
            ACCESS_TOKEN_KEY_NOT_EXIST_ERROR
        )
    }

    override suspend fun saveFamilyId(familyId: Long) {
        with(sharedPreferences.edit()) {
            putLong(FAMILY_ID_KEY, familyId)
            apply()
        }
    }

    override suspend fun loadFamilyId(): Long {
        return sharedPreferences.getLong(
            FAMILY_ID_KEY,
            DEFAULT_FAMILY_ID
        )
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        with(sharedPreferences.edit()) {
            putString(USER_NAME_KEY, userProfile.name)
            putString(USER_BIRTH_DATE_KEY, userProfile.birthDate)
            putString(USER_PROFILE_IMG_KEY, userProfile.profileImg)
            putString(USER_NICKNAME_KEY, userProfile.nickName)
            putString(USER_EMAIL_KEY, userProfile.email)
            putInt(USER_TOTAL_UPLOAD_KEY, userProfile.totalUpload)
            putInt(USER_DURATION_KEY, userProfile.duration)
            apply()
        }
    }

    override suspend fun loadUserProfile(): UserProfile {
        val userName = sharedPreferences.getString(USER_NAME_KEY, DEFAULT_STRING_USER_INFO_VALUE) ?:throw IllegalStateException(
            USER_INFO_KEY_NOT_EXIST_ERROR
        )
        val userBirthDate = sharedPreferences.getString(USER_BIRTH_DATE_KEY, DEFAULT_STRING_USER_INFO_VALUE) ?:throw IllegalStateException(
            USER_INFO_KEY_NOT_EXIST_ERROR
        )
        val userProfileImg = sharedPreferences.getString(USER_PROFILE_IMG_KEY, DEFAULT_STRING_USER_INFO_VALUE) ?:throw IllegalStateException(
            USER_INFO_KEY_NOT_EXIST_ERROR
        )
        val userNickname = sharedPreferences.getString(USER_NICKNAME_KEY, DEFAULT_STRING_USER_INFO_VALUE) ?:throw IllegalStateException(
            USER_INFO_KEY_NOT_EXIST_ERROR
        )
        val userEmail = sharedPreferences.getString(USER_EMAIL_KEY, DEFAULT_STRING_USER_INFO_VALUE) ?:throw IllegalStateException(
            USER_INFO_KEY_NOT_EXIST_ERROR
        )
        val userTotalUpload = sharedPreferences.getInt(USER_TOTAL_UPLOAD_KEY, DEFAULT_INT_USER_INFO_VALUE)
        val userDuration = sharedPreferences.getInt(USER_DURATION_KEY, DEFAULT_INT_USER_INFO_VALUE)
        if ((userTotalUpload == DEFAULT_INT_USER_INFO_VALUE) or (userDuration == DEFAULT_INT_USER_INFO_VALUE)) throw IllegalStateException(
            USER_INFO_KEY_NOT_EXIST_ERROR
        )
        return UserProfile(
            userName,
            userBirthDate,
            userProfileImg,
            userNickname,
            userEmail,
            userTotalUpload,
            userDuration
        )
    }

    override suspend fun resetPreferencesData() {
        sharedPreferences.edit().clear().commit()
    }

    override suspend fun updateUserProfile(profileEditResult: ProfileEditResult) {
        with(sharedPreferences.edit()) {
            putString(USER_NAME_KEY, profileEditResult.name)
            putString(USER_NICKNAME_KEY, profileEditResult.nickname)
            putString(USER_BIRTH_DATE_KEY, profileEditResult.birthdate)
            putString(USER_PROFILE_IMG_KEY, profileEditResult.profileImg)
            apply()
        }
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val ACCESS_TOKEN_KEY_NOT_EXIST_ERROR = "액세스 토큰이 존재하지 않습니다."
        private const val DEFAULT_TOKEN_VALUE = ""
        private const val FAMILY_ID_KEY = "family_id"
        private const val DEFAULT_FAMILY_ID = -1L

        private const val USER_NAME_KEY = "name"
        private const val USER_BIRTH_DATE_KEY = "birthDate"
        private const val USER_PROFILE_IMG_KEY = "profileImg"
        private const val USER_NICKNAME_KEY = "nickName"
        private const val USER_EMAIL_KEY = "email"
        private const val USER_TOTAL_UPLOAD_KEY = "totalUpload"
        private const val USER_DURATION_KEY = "duration"

        private const val DEFAULT_STRING_USER_INFO_VALUE = ""
        private const val DEFAULT_INT_USER_INFO_VALUE = -1

        private const val USER_INFO_KEY_NOT_EXIST_ERROR = "해당하는 가족 정보가 존재하지 않습니다."
    }
}
