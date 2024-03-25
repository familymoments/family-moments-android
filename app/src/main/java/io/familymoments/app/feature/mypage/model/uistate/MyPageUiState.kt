package io.familymoments.app.feature.mypage.model.uistate

data class MyPageUiState(
    val logoutUiState: LogoutUiState = LogoutUiState()
)

data class LogoutUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)
