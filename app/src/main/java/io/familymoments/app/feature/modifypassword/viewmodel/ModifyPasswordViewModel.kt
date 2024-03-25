package io.familymoments.app.feature.modifypassword.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.HttpResponse.INCORRECT_CURRENT_PASSWORD
import io.familymoments.app.core.network.HttpResponse.NEW_PASSWORD_SAME_AS_CURRENT
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
            currentPasswordValid = _modifyPasswordValidUiState.value.currentPasswordValid.copy(
                valid = validateCurrentPassword(password),
                warningResId = null
            )
        )
    }

    fun checkNewPassword(newPassword: String, newPasswordCheck: String) {
        val (newPasswordValid, newPasswordWarningResId) = validateNewPassword(newPassword, newPasswordCheck)
        _modifyPasswordValidUiState.value = _modifyPasswordValidUiState.value.copy(
            newPasswordValid = _modifyPasswordValidUiState.value.newPasswordValid.copy(
                valid = newPasswordValid,
                warningResId = newPasswordWarningResId
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
                    currentPasswordValid = _modifyPasswordValidUiState.value.currentPasswordValid.copy(
                        warningResId = if (it.code == INCORRECT_CURRENT_PASSWORD) WarningType.IncorrectCurrentPassword.stringResId else null,
                        reset = it.code == INCORRECT_CURRENT_PASSWORD
                    ),
                    newPasswordValid = _modifyPasswordValidUiState.value.newPasswordValid.copy(
                        warningResId = if (it.code == NEW_PASSWORD_SAME_AS_CURRENT) WarningType.NewPasswordSameAsCurrent.stringResId else null,
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
            currentPasswordValid = _modifyPasswordValidUiState.value.currentPasswordValid.copy(reset = false)
        )
    }

    fun resetNewPasswordField() {
        _modifyPasswordValidUiState.value = _modifyPasswordValidUiState.value.copy(
            newPasswordValid = _modifyPasswordValidUiState.value.newPasswordValid.copy(reset = false)
        )
    }
}
