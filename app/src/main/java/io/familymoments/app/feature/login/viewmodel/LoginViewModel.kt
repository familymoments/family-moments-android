package io.familymoments.app.feature.login.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.feature.login.model.uistate.LoginUiState
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun loginUser(username: String, password: String) {
        async(
                operation = { authRepository.loginUser(username, password) },
                onSuccess = {
                    _loginUiState.value = _loginUiState.value.copy(
                            isSuccess = true,
                            isLoading = isLoading.value,
                            loginResult = it.loginResult
                    )
                },
                onFailure = {
                    _loginUiState.value = _loginUiState.value.copy(
                            isSuccess = false,
                            isLoading = isLoading.value,
                            errorMessage = it.message
                    )
                }
        )
    }
}
