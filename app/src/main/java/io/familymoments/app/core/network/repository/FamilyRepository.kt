package io.familymoments.app.core.network.repository

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.dto.request.CreateFamilyRequest
import io.familymoments.app.core.network.dto.request.TransferPermissionRequest
import io.familymoments.app.core.network.dto.response.ApiResponse
import io.familymoments.app.core.network.dto.response.CreateFamilyResponse
import io.familymoments.app.core.network.dto.response.FamilyInfo
import io.familymoments.app.core.network.dto.response.FamilyPermission
import io.familymoments.app.core.network.dto.response.GetNicknameDdayResponse
import io.familymoments.app.core.network.dto.response.JoinFamilyResponse
import io.familymoments.app.core.network.dto.response.Member
import io.familymoments.app.core.network.dto.response.SearchFamilyByInviteLinkResponse
import io.familymoments.app.feature.modifyfamilyInfo.model.ModifyFamilyInfoRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface FamilyRepository {
    suspend fun getNicknameDday(familyId: Long): Flow<Resource<GetNicknameDdayResponse>>

    suspend fun createFamily(
        representImg: MultipartBody.Part,
        createFamilyRequest: CreateFamilyRequest
    ): Flow<Resource<CreateFamilyResponse>>

    suspend fun searchFamilyByInviteLink(
        inviteLink: String
    ): Flow<Resource<SearchFamilyByInviteLinkResponse>>

    suspend fun joinFamily(familyId: Long): Flow<Resource<JoinFamilyResponse>>

    suspend fun getFamilyInfo(familyId: Long): Flow<Resource<FamilyInfo>>

    suspend fun modifyFamilyInfo(
        familyId: Long,
        representImg: MultipartBody.Part,
        modifyFamilyInfoRequest: ModifyFamilyInfoRequest
    ): Flow<Resource<FamilyInfo>>

    suspend fun getFamilyName(): Flow<Resource<ApiResponse<String>>>

    suspend fun getFamilyMember(familyId: Long): Flow<Resource<ApiResponse<List<Member>>>>

    suspend fun transferPermission(
        familyId: Long,
        transferPermissionRequest: TransferPermissionRequest
    ): Flow<Resource<ApiResponse<String>>>

    suspend fun checkFamilyPermission(familyId: Long): Flow<Resource<ApiResponse<FamilyPermission>>>

    suspend fun removeFamilyMember(familyId: Long, userIds: List<String>): Flow<Resource<ApiResponse<String>>>

    suspend fun leaveFamily(familyId: Long): Flow<Resource<ApiResponse<String>>>
}
