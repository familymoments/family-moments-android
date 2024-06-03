package io.familymoments.app.feature.forgotpassword.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.forgotpassword.uistate.ResetUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ResetViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ResetUiState())
    val uiState = _uiState.asStateFlow()

    fun checkPasswordValid() {
        _uiState.update {
            it.copy(
                isValid = it.password == it.passwordConfirm
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun updatePasswordConfirm(passwordConfirm: String) {
        _uiState.update {
            it.copy(
                passwordConfirm = passwordConfirm
            )
        }
    }

    fun modifyPwd(id:String) {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }
        async(
            operation = {
                userRepository.modifyPwdInFindPwd(
                    id,
                    _uiState.value.password,
                    _uiState.value.passwordConfirm
                )
            },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        isSuccess = true,
                        result = it.result,
                        isLoading = false
                    )
                }
            },
            onFailure = { throwable ->
                _uiState.update {
                    it.copy(
                        isSuccess = false,
                        message = throwable.message ?: "",
                        isLoading = false
                    )
                }
            }
        )
    }

    fun showDialog(){
        _uiState.update {
            it.copy(
                showDialog = true
            )
        }
    }
}
