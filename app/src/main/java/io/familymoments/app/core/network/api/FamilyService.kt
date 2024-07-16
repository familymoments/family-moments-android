package io.familymoments.app.core.network.api

import io.familymoments.app.core.network.dto.request.CreateFamilyRequest
import io.familymoments.app.core.network.dto.request.SearchFamilyByInviteLinkRequest
import io.familymoments.app.core.network.dto.request.TransferPermissionRequest
import io.familymoments.app.core.network.dto.response.ApiResponse
import io.familymoments.app.core.network.dto.response.CreateFamilyResponse
import io.familymoments.app.core.network.dto.response.GetNicknameDdayResponse
import io.familymoments.app.core.network.dto.response.JoinFamilyResponse
import io.familymoments.app.core.network.dto.response.SearchFamilyByInviteLinkResponse
import io.familymoments.app.core.network.dto.response.FamilyInfoResponse
import io.familymoments.app.core.network.dto.response.FamilyPermission
import io.familymoments.app.core.network.dto.response.Member
import io.familymoments.app.feature.modifyfamilyInfo.model.ModifyFamilyInfoRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FamilyService {

    @GET("/families/{familyId}/created")
    suspend fun getNicknameDday(@Path("familyId") familyId: Long): Response<GetNicknameDdayResponse>

    @Multipart
    @POST("/families/family")
    suspend fun createFamily(
        @Part representImg: MultipartBody.Part,
        @Part("postFamilyReq") createFamilyRequest: CreateFamilyRequest
    ): Response<CreateFamilyResponse>

    @POST("/families/inviteCode")
    suspend fun searchFamilyByInviteLink(
        @Body searchFamilyByInviteLinkRequest: SearchFamilyByInviteLinkRequest
    ): Response<SearchFamilyByInviteLinkResponse>

    @POST("/families/{familyId}/join")
    suspend fun joinFamily(@Path("familyId") familyId: Long): Response<JoinFamilyResponse>

    @GET("/families/{familyId}")
    suspend fun getFamilyInfo(@Path("familyId") familyId: Long): Response<FamilyInfoResponse>

    @Multipart
    @PATCH("/families/{familyId}/update")
    suspend fun modifyFamilyInfo(
        @Path("familyId") familyId: Long,
        @Part("familyUpdateReq") modifyFamilyInfoRequest: ModifyFamilyInfoRequest,
        @Part representImg: MultipartBody.Part
    ): Response<FamilyInfoResponse>

    @GET("/families/{familyId}/famillyName")
    suspend fun getFamilyName(@Path("familyId") familyId: Long): Response<ApiResponse<String>>

    @GET("/families/{familyId}/users")
    suspend fun getFamilyMember(@Path("familyId") familyId: Long): Response<ApiResponse<List<Member>>>

    @PATCH("/families/{familyId}/authority")
    suspend fun transferPermission(
        @Path("familyId") familyId: Long,
        @Body transferPermissionRequest: TransferPermissionRequest
    ): Response<ApiResponse<String>>

    @GET("/families/{familyId}/authority")
    suspend fun checkFamilyPermission(@Path("familyId") familyId: Long): Response<ApiResponse<FamilyPermission>>

    @DELETE("/families/{familyId}/users")
    suspend fun removeFamilyMember(
        @Path("familyId") familyId: Long,
        @Query("userIds") userIds: List<String>
    ): Response<ApiResponse<String>>

    @DELETE("/families/{familyId}/withdraw")
    suspend fun leaveFamily(@Path("familyId") familyId: Long): Response<ApiResponse<String>>
}
