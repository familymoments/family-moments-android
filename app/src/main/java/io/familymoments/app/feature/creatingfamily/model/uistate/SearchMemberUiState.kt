package io.familymoments.app.feature.creatingfamily.model.uistate

import io.familymoments.app.feature.creatingfamily.model.response.Member

data class SearchMemberUiState(
    val isSuccess: Boolean? = null,
    val isLoading: Boolean? = null,
    val errorMessage: String? = "",
    val members: List<Member> = listOf()
)
