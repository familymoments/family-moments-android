package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.SignInService
import io.familymoments.app.core.network.dto.request.CheckEmailRequest
import io.familymoments.app.core.network.dto.request.CheckIdRequest
import io.familymoments.app.core.network.dto.request.SignUpRequest
import io.familymoments.app.core.network.dto.response.CheckEmailResponse
import io.familymoments.app.core.network.dto.response.CheckIdResponse
import io.familymoments.app.core.network.dto.response.SignUpResponse
import io.familymoments.app.core.network.repository.SignInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val signInService: SignInService
) : SignInRepository {
    override suspend fun checkId(id: String): Flow<Resource<CheckIdResponse>> {
        return flow {
            emit(Resource.Loading)
            val result = signInService.checkId(CheckIdRequest(id))

            if (result.isSuccess) {
                emit(Resource.Success(result))
            } else {
                throw Throwable(result.message)
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun checkEmail(email: String): Flow<Resource<CheckEmailResponse>> {
        return flow {
            emit(Resource.Loading)

            val result =
                signInService.checkEmail(CheckEmailRequest(email))

            if (result.isSuccess) {
                emit(Resource.Success(result))
            } else {
                emit(Resource.Fail(Throwable(result.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun executeSignUp(
        profileImg: MultipartBody.Part,
        signUpRequest: SignUpRequest
    ): Flow<Resource<SignUpResponse>> {
        return flow {
            emit(Resource.Loading)

            val result = signInService.executeSignUp(profileImg, signUpRequest)

            if (result.isSuccess) {
                emit(Resource.Success(result))
            } else {
                emit(Resource.Fail(Throwable(result.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

}
