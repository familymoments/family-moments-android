package io.familymoments.app.feature.modifypassword.model.uistate

import androidx.annotation.StringRes

data class ModifyPasswordValidUiState(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val errorMessage: String? = null,
    val currentPasswordValid: ModifyPasswordValid = ModifyPasswordValid(),
    val newPasswordValid: ModifyPasswordValid = ModifyPasswordValid()
)

data class ModifyPasswordValid (
    val valid: Boolean = false,
    @StringRes val warningResId: Int? = null,
    val reset: Boolean = false
)
