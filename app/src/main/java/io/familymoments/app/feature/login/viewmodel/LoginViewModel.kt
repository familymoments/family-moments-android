package io.familymoments.app.feature.login.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.core.util.DEFAULT_FCM_TOKEN_VALUE
import io.familymoments.app.feature.login.uistate.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    init {
        getFCMToken()
    }

    fun loginUser(username: String, password: String) {
        async(
            operation = {
                val fcmToken = userInfoPreferencesDataSource.loadFCMToken()
                userRepository.loginUser(username.trimEnd(), password.trimEnd(), fcmToken)
            },
            onSuccess = {
                _loginUiState.value = _loginUiState.value.copy(
                    isSuccess = true,
                    isLoading = isLoading.value,
                    loginResult = it.result
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

    // errorMessage가 더이상 나타나지 않도록 하기 위함
    fun updateSuccessNull() {
        if (_loginUiState.value.isSuccess != null) {
            _loginUiState.value = _loginUiState.value.copy(isSuccess = null)
        }
    }

    private fun getFCMToken() {
        viewModelScope.launch {
            try {
                if (userInfoPreferencesDataSource.loadFCMToken() == DEFAULT_FCM_TOKEN_VALUE) {
                    val fcmToken = FirebaseMessaging.getInstance().token.await()
                    userInfoPreferencesDataSource.saveFCMToken(fcmToken)
                }
            } catch (e: Exception) {
                Timber.tag("fcm-token").e("getFCMToken: ${e.message}")
            }
        }
    }
}
