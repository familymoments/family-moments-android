package io.familymoments.app.feature.login.uistate

import androidx.compose.runtime.Immutable
import io.familymoments.app.core.network.dto.response.LoginResult

@Immutable
data class LoginUiState(
    val isLoading: Boolean? = null,
    val isSuccess: Boolean? = null,
    val isNeedToSignUp: Boolean? = null,
    val errorMessage: String? = null,
    val loginResult: LoginResult? = null,
    val socialType: String = "",
    val socialToken: String = "",
)
