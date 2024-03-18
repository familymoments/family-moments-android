package io.familymoments.app.feature.profile.model.uistate

import io.familymoments.app.core.network.model.UserProfile

data class ProfileViewUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val userProfile: UserProfile = UserProfile()
)
