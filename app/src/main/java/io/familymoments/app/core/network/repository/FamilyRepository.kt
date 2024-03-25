package io.familymoments.app.core.network.repository

import io.familymoments.app.core.network.Resource
import io.familymoments.app.feature.creatingfamily.model.CreateFamilyRequest
import io.familymoments.app.feature.creatingfamily.model.CreateFamilyResponse
import io.familymoments.app.feature.joiningfamily.model.SearchFamilyByInviteLinkResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface FamilyRepository {
    suspend fun createFamily(
        representImg: MultipartBody.Part,
        createFamilyRequest: CreateFamilyRequest
    ): Flow<Resource<CreateFamilyResponse>>

    suspend fun searchFamilyByInviteLink(
        inviteLink:String
    ):Flow<Resource<SearchFamilyByInviteLinkResponse>>
}
