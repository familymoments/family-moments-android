package io.familymoments.app.core.network.api

import io.familymoments.app.core.network.dto.request.SearchFamilyByInviteLinkRequest
import io.familymoments.app.core.network.dto.response.ApiResponse
import io.familymoments.app.core.network.dto.response.SearchFamilyByInviteLinkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AlertService {
    @GET("/alarms/uploadAlarm")
    suspend fun uploadAlarm(): Response<ApiResponse<Any>>
}
