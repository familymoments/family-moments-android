package io.familymoments.app.feature.joiningfamily.uistate

import androidx.compose.runtime.Immutable
import io.familymoments.app.core.network.dto.response.SearchFamilyByInviteLinkResult

@Immutable
data class JoinFamilyUiState(
    val selectedFamilyId:Long? = null,
    val searchFamilyByInviteLinkUiState: SearchFamilyByInviteLinkUiState = SearchFamilyByInviteLinkUiState(),
    val joinFamilyExecuteUiState: JoinFamilyExecuteUiState = JoinFamilyExecuteUiState()
)

@Immutable
data class SearchFamilyByInviteLinkUiState(
    val isSuccess: Boolean? = null,
    val isLoading: Boolean? = null,
    val errorMessage: String? = "",
    val result: SearchFamilyByInviteLinkResult? = null
)

@Immutable
data class JoinFamilyExecuteUiState(
    val isSuccess: Boolean? = null,
    val isLoading: Boolean? = null,
    val errorMessage: String? = "",
    val result: String? = null
)
