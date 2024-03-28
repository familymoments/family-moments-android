package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.HttpResponse
import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.FamilyService
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.feature.creatingfamily.model.CreateFamilyRequest
import io.familymoments.app.feature.creatingfamily.model.CreateFamilyResponse
import io.familymoments.app.feature.home.model.GetNicknameDdayResponse
import io.familymoments.app.feature.joiningfamily.model.JoinFamilyResponse
import io.familymoments.app.feature.joiningfamily.model.SearchFamilyByInviteLinkRequest
import io.familymoments.app.feature.joiningfamily.model.SearchFamilyByInviteLinkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class FamilyRepositoryImpl @Inject constructor(
    private val familyService: FamilyService
) : FamilyRepository {
    override suspend fun getNicknameDday(familyId: Long): Flow<Resource<GetNicknameDdayResponse>> {
        return flow {
            emit(Resource.Loading)
            val response = familyService.getNicknameDday(familyId)
            val responseBody = response.body() ?: GetNicknameDdayResponse()

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
            val response = familyService.searchFamilyByInviteLink(SearchFamilyByInviteLinkRequest(inviteLink))
            val responseBody = response.body() ?: SearchFamilyByInviteLinkResponse()

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
                    emit(Resource.Success(responseBody))
                } else {
                    emit(Resource.Fail(Throwable(responseBody.message)))
                }
            } else {
                emit(Resource.Fail(Throwable(response.message())))
            }
        }
    }

}
