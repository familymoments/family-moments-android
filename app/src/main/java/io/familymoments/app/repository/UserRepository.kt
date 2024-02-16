package io.familymoments.app.repository

import io.familymoments.app.feature.login.model.response.LoginResponse
import io.familymoments.app.core.network.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun loginUser(username: String, password: String): Flow<Resource<LoginResponse>>
    suspend fun checkAccessTokenValidation(): Flow<Resource<Unit>>
    suspend fun reissueAccessToken(): Flow<Resource<Unit>>
}
