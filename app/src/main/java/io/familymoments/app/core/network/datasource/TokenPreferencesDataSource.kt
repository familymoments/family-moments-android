package io.familymoments.app.core.network.datasource

interface TokenPreferencesDataSource {
    suspend fun saveAccessToken(token: String)
    suspend fun loadAccessToken(): String
}
