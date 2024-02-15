package io.familymoments.app.ui.splash.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.model.uistate.SplashUiState
import io.familymoments.app.repository.UserRepository
import io.familymoments.app.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState = _splashUiState.asStateFlow()
    fun checkUserValidation() {
        async(
            operation = { userRepository.checkAccessTokenValidation() },
            onSuccess = {
                _splashUiState.value = _splashUiState.value.copy(
                    isLoading = isLoading.value,
                    isSuccess = true
                )
            },
            onFailure = {
                _splashUiState.value = _splashUiState.value.copy(
                    isLoading = isLoading.value,
                    isSuccess = false,
                    error = it
                )
            }
        )
    }
}
