package io.familymoments.app.feature.removemember.uistate

data class RemoveFamilyMemberConfirmUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = "",
    val userIds: List<String> = emptyList()
)
