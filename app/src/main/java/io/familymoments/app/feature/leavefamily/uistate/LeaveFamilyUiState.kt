package io.familymoments.app.feature.leavefamily.uistate

data class LeaveFamilyUiState(
    val isSuccess: Boolean? = null,
    val errorMessage: String? = "",
    val isOwner: Boolean = false,
)
