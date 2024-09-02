package io.familymoments.app.core.network.datasource

import android.content.SharedPreferences
import io.familymoments.app.core.network.dto.response.ProfileEditResult
import io.familymoments.app.core.network.dto.response.UserProfile
import io.familymoments.app.core.util.DEFAULT_FAMILY_ID_VALUE
import io.familymoments.app.core.util.DEFAULT_FCM_TOKEN_VALUE
import io.familymoments.app.core.util.DEFAULT_TOKEN_VALUE
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

    override suspend fun saveFCMToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(FCM_TOKEN_KEY, token)
            apply()
        }
    }

    override suspend fun loadFCMToken(): String {
        return sharedPreferences.getString(
            FCM_TOKEN_KEY,
            DEFAULT_FCM_TOKEN_VALUE
        ) ?: throw IllegalStateException(
            FCM_TOKEN_KEY_NOT_EXIST_ERROR
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
            DEFAULT_FAMILY_ID_VALUE
        )
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        with(sharedPreferences.edit()) {
            putString(USER_PROFILE_IMG_KEY, userProfile.profileImg)
            putString(USER_NICKNAME_KEY, userProfile.nickName)
            putString(USER_EMAIL_KEY, userProfile.email)
            putInt(USER_TOTAL_UPLOAD_KEY, userProfile.totalUpload)
            putInt(USER_DURATION_KEY, userProfile.duration)
            apply()
        }
    }

    override suspend fun loadUserProfile(): UserProfile {
        val userName =
            sharedPreferences.getString(USER_NAME_KEY, DEFAULT_STRING_USER_INFO_VALUE) ?: throw IllegalStateException(
                USER_INFO_KEY_NOT_EXIST_ERROR
            )
        val userBirthDate = sharedPreferences.getString(USER_BIRTH_DATE_KEY, DEFAULT_STRING_USER_INFO_VALUE)
            ?: throw IllegalStateException(
                USER_INFO_KEY_NOT_EXIST_ERROR
            )
        val userProfileImg = sharedPreferences.getString(USER_PROFILE_IMG_KEY, DEFAULT_STRING_USER_INFO_VALUE)
            ?: throw IllegalStateException(
                USER_INFO_KEY_NOT_EXIST_ERROR
            )
        val userNickname = sharedPreferences.getString(USER_NICKNAME_KEY, DEFAULT_STRING_USER_INFO_VALUE)
            ?: throw IllegalStateException(
                USER_INFO_KEY_NOT_EXIST_ERROR
            )
        val userEmail =
            sharedPreferences.getString(USER_EMAIL_KEY, DEFAULT_STRING_USER_INFO_VALUE) ?: throw IllegalStateException(
                USER_INFO_KEY_NOT_EXIST_ERROR
            )
        val userTotalUpload = sharedPreferences.getInt(USER_TOTAL_UPLOAD_KEY, DEFAULT_INT_USER_INFO_VALUE)
        val userDuration = sharedPreferences.getInt(USER_DURATION_KEY, DEFAULT_INT_USER_INFO_VALUE)
        if ((userTotalUpload == DEFAULT_INT_USER_INFO_VALUE) or (userDuration == DEFAULT_INT_USER_INFO_VALUE)) throw IllegalStateException(
            USER_INFO_KEY_NOT_EXIST_ERROR
        )
        return UserProfile(
            userProfileImg,
            userNickname,
            userEmail,
            userTotalUpload,
            userDuration
        )
    }

    override suspend fun loadUserProfileImg(): String {
        return sharedPreferences.getString(USER_PROFILE_IMG_KEY, DEFAULT_STRING_USER_INFO_VALUE)
            ?: throw IllegalStateException(USER_INFO_KEY_NOT_EXIST_ERROR)
    }

    override fun resetPreferencesData() {
        sharedPreferences.edit().clear().apply()
    }

    override fun removeFamilyId() {
        sharedPreferences.edit().remove(FAMILY_ID_KEY).apply()
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

    override suspend fun saveRefreshToken(refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString(REFRESH_TOKEN_KEY, refreshToken)
            apply()
        }
    }

    override suspend fun loadRefreshToken(): String {
        return sharedPreferences.getString(
            REFRESH_TOKEN_KEY,
            DEFAULT_TOKEN_VALUE
        ) ?: throw IllegalStateException(
            ACCESS_TOKEN_KEY_NOT_EXIST_ERROR
        )
    }

    override fun saveLoginType(loginType: String) {
        sharedPreferences.edit().putString(SOCIAL_LOGIN_TYPE_KEY, loginType).apply()
    }

    override fun loadLoginType(): String {
        return sharedPreferences.getString(
            SOCIAL_LOGIN_TYPE_KEY,
            ""
        ) ?: throw IllegalStateException(
            "소셜 로그인 타입이 존재하지 않습니다."
        )
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val ACCESS_TOKEN_KEY_NOT_EXIST_ERROR = "액세스 토큰이 존재하지 않습니다."
        private const val FAMILY_ID_KEY = "family_id"
        private const val FCM_TOKEN_KEY = "fcm_token"
        private const val FCM_TOKEN_KEY_NOT_EXIST_ERROR = "FCM 토큰이 존재하지 않습니다."

        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val SOCIAL_LOGIN_TYPE_KEY = "social_login_type"

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
