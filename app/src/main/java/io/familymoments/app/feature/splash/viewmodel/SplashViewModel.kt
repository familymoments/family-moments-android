package io.familymoments.app.feature.splash.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.feature.splash.model.SplashUiState
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {
    private val _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState = _splashUiState.asStateFlow()
    fun checkUserValidation() {
        async(
            operation = { authRepository.checkAccessTokenValidation() },
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
