package io.familymoments.app.feature.modifypassword.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.modifypassword.model.WarningType
import io.familymoments.app.feature.modifypassword.model.mapper.toRequest
import io.familymoments.app.feature.modifypassword.model.uistate.ModifyPasswordUiState
import io.familymoments.app.feature.modifypassword.model.uistate.ModifyPasswordValidUiState
import io.familymoments.app.feature.modifypassword.validateCurrentPassword
import io.familymoments.app.feature.modifypassword.validateNewPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ModifyPasswordViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {
    private val _modifyPasswordValidUiState: MutableStateFlow<ModifyPasswordValidUiState> = MutableStateFlow(ModifyPasswordValidUiState())
    val modifyPasswordValidUiState: StateFlow<ModifyPasswordValidUiState> = _modifyPasswordValidUiState.asStateFlow()

    private val _modifyPasswordUiState: MutableStateFlow<ModifyPasswordUiState> = MutableStateFlow(ModifyPasswordUiState())
    val modifyPasswordUiState: StateFlow<ModifyPasswordUiState> = _modifyPasswordUiState.asStateFlow()

    fun checkCurrentPassword(password: String) {
        _modifyPasswordValidUiState.value = _modifyPasswordValidUiState.value.copy(
            currentPassword = _modifyPasswordValidUiState.value.currentPassword.copy(
                valid = validateCurrentPassword(password),
                warning = null
            )
        )
    }

    fun checkNewPassword(newPassword: String, newPasswordCheck: String) {
        val (newPasswordValid, newPasswordWarning) = validateNewPassword(newPassword, newPasswordCheck)
        _modifyPasswordValidUiState.value = _modifyPasswordValidUiState.value.copy(
            newPassword = _modifyPasswordValidUiState.value.newPassword.copy(
                valid = newPasswordValid,
                warning = newPasswordWarning
            ),
        )
    }

    fun requestModifyPassword(passwordUiState: ModifyPasswordUiState) {
        async(
            operation = { userRepository.modifyPassword(passwordUiState.toRequest()) },
            onSuccess = {
                _modifyPasswordValidUiState.value = _modifyPasswordValidUiState.value.copy(
                    isSuccess = it.isSuccess,
                    code = it.code,
                    currentPassword = _modifyPasswordValidUiState.value.currentPassword.copy(
                        warning = if (it.code == INCORRECT_CURRENT_PASSWORD) WarningType.IncorrectCurrentPassword.stringResId else null,
                        reset = it.code == INCORRECT_CURRENT_PASSWORD
                    ),
                    newPassword = _modifyPasswordValidUiState.value.newPassword.copy(
                        warning = if (it.code == NEW_PASSWORD_SAME_AS_CURRENT) WarningType.NewPasswordSameAsCurrent.stringResId else null,
                        reset = it.code == NEW_PASSWORD_SAME_AS_CURRENT
                    ),
                )
            },
            onFailure = {
                _modifyPasswordValidUiState.value = _modifyPasswordValidUiState.value.copy(
                    isSuccess = false,
                    errorMessage = it.message,
                )
            }
        )
    }

    fun updateCurrentPassword(password: String) {
        _modifyPasswordUiState.value = _modifyPasswordUiState.value.copy(password = password)
    }

    fun updateNewPassword(newPassword: String, newPasswordCheck: String) {
        _modifyPasswordUiState.value = _modifyPasswordUiState.value.copy(newPassword = newPassword, newPasswordCheck = newPasswordCheck)
    }

    fun resetCurrentPasswordField() {
        _modifyPasswordValidUiState.value = _modifyPasswordValidUiState.value.copy(
            currentPassword = _modifyPasswordValidUiState.value.currentPassword.copy(reset = false)
        )
    }

    fun resetNewPasswordField() {
        _modifyPasswordValidUiState.value = _modifyPasswordValidUiState.value.copy(
            newPassword = _modifyPasswordValidUiState.value.newPassword.copy(reset = false)
        )
    }

    companion object {
        private const val INCORRECT_CURRENT_PASSWORD = 4000
        private const val NEW_PASSWORD_SAME_AS_CURRENT = 4003
    }
}
