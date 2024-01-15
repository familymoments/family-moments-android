package io.familymoments.app.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.model.LoginUiState
import io.familymoments.app.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
        private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun loginUser(username: String, password: String) {
        async(
                operation = { userRepository.loginUser(username, password) },
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