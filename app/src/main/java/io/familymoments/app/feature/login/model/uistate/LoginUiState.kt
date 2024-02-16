package io.familymoments.app.feature.login.model.uistate

import io.familymoments.app.feature.login.model.response.LoginResult

data class LoginUiState(
    val isLoading: Boolean? = null,
    val isSuccess: Boolean? = null,
    val errorMessage: String? = null,
    val loginResult: LoginResult? = null,
)
