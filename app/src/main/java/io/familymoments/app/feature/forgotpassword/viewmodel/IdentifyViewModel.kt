package io.familymoments.app.feature.forgotpassword.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.forgotpassword.uistate.IdentifyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class IdentifyViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(IdentifyUiState())
    val uiState = _uiState.asStateFlow()

    fun checkIdExist(userId: String) {
        async(
            operation = {
                userRepository.checkIdExist(userId)
            },
            onSuccess = {
                _uiState.update {
                    it.copy(
                        isSuccess = true
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        isSuccess = false,
                        message = it.message
                    )
                }
            }
        )
    }

    fun resetSuccess() {
        _uiState.update {
            it.copy(
                isSuccess = null
            )
        }
    }
}
