package io.familymoments.app.feature.familyinvitationlink.uistate

data class FamilyInvitationLinkUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val invitationLink: String = ""
)
