package io.familymoments.app.core.network.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class TokenPreferencesDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
):TokenPreferencesDataSource {
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
            KEY_NOT_EXIST_ERROR
        )
    }
    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val KEY_NOT_EXIST_ERROR = "해당 키 값이 존재하지 않습니다."
        private const val DEFAULT_TOKEN_VALUE = ""
    }
}
