package io.familymoments.app.feature.forgotpassword.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.forgotpassword.uistate.VerifyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(VerifyUiState())
    val uiState = _uiState.asStateFlow()

    fun sendEmail(name: String, email: String) {
        async(
            operation = {
                userRepository.sendEmail(name, email)
            },
            onSuccess = {response->
                _uiState.update {
                    it.copy(
                        sendEmailUiState = it.sendEmailUiState.copy(
                            isSuccess = true,
                            result = response.result
                        )
                    )
                }
            },
            onFailure = { throwable ->
                _uiState.update {
                    it.copy(
                        sendEmailUiState = it.sendEmailUiState.copy(
                            isSuccess = false,
                            message = throwable.message ?: ""
                        )
                    )
                }
            }
        )
    }

    fun resetSendEmailSuccess() {
        _uiState.update {
            it.copy(
                sendEmailUiState = it.sendEmailUiState.copy(
                    isSuccess = null
                )
            )
        }
    }
}
