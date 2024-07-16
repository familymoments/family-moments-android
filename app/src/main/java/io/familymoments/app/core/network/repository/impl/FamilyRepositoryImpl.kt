package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.HttpResponse
import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.FamilyService
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.dto.request.CreateFamilyRequest
import io.familymoments.app.core.network.dto.request.SearchFamilyByInviteLinkRequest
import io.familymoments.app.core.network.dto.request.TransferPermissionRequest
import io.familymoments.app.core.network.dto.response.ApiResponse
import io.familymoments.app.core.network.dto.response.CreateFamilyResponse
import io.familymoments.app.core.network.dto.response.FamilyInfo
import io.familymoments.app.core.network.dto.response.FamilyInfoResponse
import io.familymoments.app.core.network.dto.response.FamilyPermission
import io.familymoments.app.core.network.dto.response.GetNicknameDdayResponse
import io.familymoments.app.core.network.dto.response.JoinFamilyResponse
import io.familymoments.app.core.network.dto.response.Member
import io.familymoments.app.core.network.dto.response.SearchFamilyByInviteLinkResponse
import io.familymoments.app.core.network.dto.response.getResourceFlow
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.feature.modifyfamilyInfo.model.ModifyFamilyInfoRequest
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

    override suspend fun modifyFamilyInfo(
        familyId: Long,
        representImg: MultipartBody.Part,
        modifyFamilyInfoRequest: ModifyFamilyInfoRequest
    ): Flow<Resource<FamilyInfo>> {
        return flow {
            emit(Resource.Loading)
            val response = familyService.modifyFamilyInfo(familyId, modifyFamilyInfoRequest, representImg)
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

    override suspend fun getFamilyName(): Flow<Resource<ApiResponse<String>>> {
        val familyId = userInfoPreferencesDataSource.loadFamilyId()
        val response = familyService.getFamilyName(familyId)
        return getResourceFlow(response)
    }

    override suspend fun getFamilyMember(familyId: Long): Flow<Resource<ApiResponse<List<Member>>>> {
        val response = familyService.getFamilyMember(familyId)
        return getResourceFlow(response)
    }

    override suspend fun transferPermission(
        familyId: Long,
        transferPermissionRequest: TransferPermissionRequest
    ): Flow<Resource<ApiResponse<String>>> {
        val response = familyService.transferPermission(familyId, transferPermissionRequest)
        return getResourceFlow(response)
    }

    override suspend fun checkFamilyPermission(familyId: Long): Flow<Resource<ApiResponse<FamilyPermission>>> {
        val response = familyService.checkFamilyPermission(familyId)
        return getResourceFlow(response)
    }

    override suspend fun removeFamilyMember(familyId: Long, userIds: List<String>): Flow<Resource<ApiResponse<String>>> {
        val response = familyService.removeFamilyMember(familyId, userIds)
        return getResourceFlow(response)
    }

    override suspend fun leaveFamily(familyId: Long): Flow<Resource<ApiResponse<String>>> {
        val response = familyService.leaveFamily(familyId)
        return getResourceFlow(response)
    }
}
