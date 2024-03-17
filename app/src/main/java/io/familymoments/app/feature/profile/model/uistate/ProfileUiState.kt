package io.familymoments.app.feature.profile.model.uistate

data class ProfileViewUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val nickname: String = "",
    val email: String = "",
    val totalUpload: Int = 0,
    val duration: Int = 0,
    val profileImg: String = "",
)
