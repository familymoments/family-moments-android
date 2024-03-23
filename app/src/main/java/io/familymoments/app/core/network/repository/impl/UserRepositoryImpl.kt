package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.UserService
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.model.AuthErrorResponse
import io.familymoments.app.core.network.model.UserProfileResponse
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.login.model.request.LoginRequest
import io.familymoments.app.feature.login.model.response.LoginResponse
import io.familymoments.app.feature.profile.model.request.ProfileEditRequest
import io.familymoments.app.feature.profile.model.response.ProfileEditResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.Headers
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : UserRepository {
    override suspend fun loginUser(
        username: String,
        password: String
    ): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = userService.loginUser(LoginRequest(username, password))
            val responseBody = response.body() ?: LoginResponse()

            if (responseBody.isSuccess) {
                saveAccessToken(response.headers())
                val familyId: Long? = responseBody.loginResult.familyId
                if (familyId != null) {
                    userInfoPreferencesDataSource.saveFamilyId(familyId)
                }
                loadUserProfile(familyId).collect{ result ->
                    if (result is Resource.Success){
                        userInfoPreferencesDataSource.saveUserProfile(result.data.result)
                    }
                    if (result is Resource.Fail){
                        emit(Resource.Fail(Throwable(result.exception.message)))
                    }

                }
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }

        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    private suspend fun saveAccessToken(headers: Headers) {
        val accessToken = headers[KEY_ACCESS_TOKEN]
            ?: throw IllegalStateException(GET_ACCESS_TOKEN_ERROR)
        userInfoPreferencesDataSource.saveAccessToken(accessToken)
    }

    override suspend fun reissueAccessToken(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading)
            val response = userService.reissueAccessToken()
            if (response.code() == 200) {
                // refresh token 을 기반으로 access token 재발급 성공
                kotlin.runCatching {
                    saveAccessToken(response.headers())
                }.onSuccess {
                    emit(Resource.Success(Unit))
                }.onFailure {
                    emit(Resource.Fail(it))
                }
            } else if (response.code() == 471) {
                // refresh token 까지 만로 됐다는 것을 뜻함. 따라서 즉 재로그인 필요
                // screen 에서 RefreshTokenExpiration 오류 발생 확인되면 로그인 화면으로 이동
                emit(Resource.Fail(AuthErrorResponse.RefreshTokenExpiration))
            } else {
                emit(Resource.Fail(Throwable(response.message())))
            }
        }.catch { e ->
            emit(Resource.Fail(Throwable(e.message)))
        }
    }

    override suspend fun loadUserProfile(familyId: Long?): Flow<Resource<UserProfileResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = userService.loadUserProfile(familyId)
            val responseBody = response.body() ?: UserProfileResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }

        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun editProfile(
        profileEditRequest: ProfileEditRequest,
        profileImg: MultipartBody.Part
    ): Flow<Resource<ProfileEditResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = userService.editProfile(profileEditRequest, profileImg)
            val responseBody = response.body() ?: ProfileEditResponse()
            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "X-AUTH-TOKEN"
        private const val GET_ACCESS_TOKEN_ERROR = "Fail to get Access Token"
    }
}
