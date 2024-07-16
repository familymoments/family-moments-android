package io.familymoments.app.feature.removemember.uistate

import io.familymoments.app.core.network.dto.response.Member

data class RemoveFamilyMemberUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = "",
    val members: List<Member> = emptyList(),
    val isOwner: Boolean = true,
)
