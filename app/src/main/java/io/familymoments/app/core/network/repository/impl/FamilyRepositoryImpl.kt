package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.HttpResponse
import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.FamilyService
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.dto.request.CreateFamilyRequest
import io.familymoments.app.core.network.dto.request.SearchFamilyByInviteLinkRequest
import io.familymoments.app.core.network.dto.response.CreateFamilyResponse
import io.familymoments.app.core.network.dto.response.FamilyInfo
import io.familymoments.app.core.network.dto.response.GetNicknameDdayResponse
import io.familymoments.app.core.network.dto.response.JoinFamilyResponse
import io.familymoments.app.core.network.dto.response.SearchFamilyByInviteLinkResponse
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.core.network.dto.response.FamilyInfoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class FamilyRepositoryImpl @Inject constructor(
    private val familyService: FamilyService,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : FamilyRepository {
    override suspend fun getNicknameDday(familyId: Long): Flow<Resource<GetNicknameDdayResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = familyService.getNicknameDday(familyId)
            val responseBody =
                response.body() ?: GetNicknameDdayResponse()

            if (responseBody.isSuccess) {
                emit(Resource.Success(responseBody))
            } else {
                emit(Resource.Fail(Throwable(responseBody.message)))
            }
        }.catch { e ->
            emit(Resource.Fail(e))
        }
    }

    override suspend fun createFamily(
        representImg: MultipartBody.Part,
        createFamilyRequest: CreateFamilyRequest
    ): Flow<Resource<CreateFamilyResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = familyService.createFamily(representImg, createFamilyRequest)
            val responseBody = response.body() ?: CreateFamilyResponse()

            if (response.code() == HttpResponse.SUCCESS) {
                if (responseBody.isSuccess) {
                    emit(Resource.Success(responseBody))
                } else {
                    emit(Resource.Fail(Throwable(responseBody.message)))
                }
            } else {
                emit(Resource.Fail(Throwable(response.message())))
            }
        }
    }

    override suspend fun searchFamilyByInviteLink(inviteLink: String): Flow<Resource<SearchFamilyByInviteLinkResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = familyService.searchFamilyByInviteLink(
                SearchFamilyByInviteLinkRequest(
                    inviteLink
                )
            )
            val responseBody =
                response.body() ?: SearchFamilyByInviteLinkResponse()

            if (response.code() == HttpResponse.SUCCESS) {
                if (responseBody.isSuccess) {
                    emit(Resource.Success(responseBody))
                } else {
                    emit(Resource.Fail(Throwable(responseBody.message)))
                }
            } else {
                emit(Resource.Fail(Throwable(response.message())))
            }
        }
    }

    override suspend fun joinFamily(familyId: Long): Flow<Resource<JoinFamilyResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = familyService.joinFamily(familyId)
            val responseBody = response.body() ?: JoinFamilyResponse()

            if (response.code() == HttpResponse.SUCCESS) {
                if (responseBody.isSuccess) {
                    userInfoPreferencesDataSource.saveFamilyId(familyId)
                    emit(Resource.Success(responseBody))
                } else {
                    emit(Resource.Fail(Throwable(responseBody.message)))
                }
            } else {
                emit(Resource.Fail(Throwable(response.message())))
            }
        }
    }

    override suspend fun getFamilyInfo(familyId: Long): Flow<Resource<FamilyInfo>> {
        return flow {
            emit(Resource.Loading)
            val response = familyService.getFamilyInfo(familyId)
            if (response.code() == HttpResponse.SUCCESS) {
                val responseBody = response.body()?: FamilyInfoResponse()
                if (responseBody.isSuccess) {
                    emit(Resource.Success(responseBody.result))
                } else {
                    emit(Resource.Fail(Throwable(responseBody.message)))
                }
            } else {
                emit(Resource.Fail(Throwable(response.message())))
            }
        }
    }

}
