package io.familymoments.app.repository.impl

import io.familymoments.app.feature.login.model.request.LoginRequest
import io.familymoments.app.feature.login.model.response.LoginResponse
import io.familymoments.app.model.response.UserErrorResponse
import io.familymoments.app.core.network.UserService
import io.familymoments.app.core.network.Resource
import io.familymoments.app.repository.UserRepository
import io.familymoments.app.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.Headers
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val tokenRepository: TokenRepository
) : UserRepository {
    override suspend fun loginUser(
        username: String,
        password: String,
    ): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = userService.loginUser(LoginRequest(username, password))
            val responseBody = response.body() ?: LoginResponse()

            if (responseBody.isSuccess) {
                saveAccessToken(response.headers())
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }

        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun checkAccessTokenValidation(): Flow<Resource<Unit>> {

        return flow {
            emit(Resource.Loading)
            val response = userService.checkAccessTokenValidation()
            if (response.code() == 200) {
                emit(Resource.Success(Unit))
            } else if (response.code() == 401) {
                reissueAccessToken()
            }else{
                emit(Resource.Fail(UserErrorResponse.CommonError(response.message())))
            }
        }.catch { e ->
            emit(Resource.Fail(UserErrorResponse.CommonError(e.message?:"")))
        }
    }

    override suspend fun reissueAccessToken(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading)
            val response = userService.reissueAccessToken()
            if (response.code() == 200) {
                emit(Resource.Success(Unit))
            } else if (response.code() == 471) {
                // 로그인 화면 전환
                emit(Resource.Fail(UserErrorResponse.RefreshTokenExpiration))
            }else{
                emit(Resource.Fail(UserErrorResponse.CommonError(response.message())))
            }
        }.catch { e ->
            emit(Resource.Fail(UserErrorResponse.CommonError(e.message?:"")))
        }
    }

    private suspend fun saveAccessToken(headers: Headers) {
        val accessToken = headers[KEY_ACCESS_TOKEN]
            ?: throw IllegalStateException(GET_ACCESS_TOKEN_ERROR)
        tokenRepository.saveAccessToken(accessToken)
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "X-AUTH-TOKEN"
        private const val GET_ACCESS_TOKEN_ERROR = "Fail to get Access Token"
    }
}
