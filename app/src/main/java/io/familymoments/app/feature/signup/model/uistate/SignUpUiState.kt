package io.familymoments.app.feature.signup.model.uistate

import android.graphics.Bitmap
import io.familymoments.app.core.component.CheckedStatus

data class SignUpUiState(
    val signUpInfoUiState: SignUpInfoUiState = SignUpInfoUiState(),
    val signUpValidatedUiState: SignUpValidatedUiState = SignUpValidatedUiState(),
    val signUpTermUiState: SignUpTermUiState = SignUpTermUiState()
)

data class SignUpInfoUiState(
    val id: String = "",
    val password: String = "",
    val name: String = "",
    val email: String = "",
    val birthDay: String = "",
    val nickname: String = "",
    val bitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
)

data class SignUpValidatedUiState(
    val userIdValidated: Boolean = false,
    val userIdDuplicated: Boolean? = null,
    val passwordValidated: Boolean = false,
    val emailValidated: Boolean = false,
    val emailDuplicated: Boolean? = null,
    val nicknameValidated: Boolean = false
)

data class SignUpTermUiState(
    val isEssential: Boolean = false,
    val description: Int = 0,
    val checkedStatus: CheckedStatus = CheckedStatus.UNCHECKED
)

