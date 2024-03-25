package io.familymoments.app.feature.joiningfamily.model

data class SearchFamilyByInviteLinkUiState(
    val isSuccess: Boolean? = null,
    val isLoading: Boolean? = null,
    val errorMessage: String? = "",
    val result: SearchFamilyByInviteLinkResult? = null
)
