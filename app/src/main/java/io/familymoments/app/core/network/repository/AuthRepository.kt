package io.familymoments.app.core.network.repository

import io.familymoments.app.core.network.Resource
import io.familymoments.app.feature.login.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginUser(username: String, password: String): Flow<Resource<LoginResponse>>
    suspend fun checkAccessTokenValidation(): Flow<Resource<Unit>>
    suspend fun reissueAccessToken(): Flow<Resource<Unit>>
}
