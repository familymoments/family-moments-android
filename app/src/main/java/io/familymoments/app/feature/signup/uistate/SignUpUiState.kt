package io.familymoments.app.feature.signup.uistate

import androidx.compose.runtime.Immutable
import io.familymoments.app.core.component.CheckedStatus
import java.io.File

@Immutable
data class SignUpUiState(
    val signUpInfoUiState: SignUpInfoUiState = SignUpInfoUiState(),
    val signUpValidatedUiState: SignUpValidatedUiState = SignUpValidatedUiState(),
    val signUpTermUiState: SignUpTermUiState = SignUpTermUiState(),
    val signUpResultUiState: SignUpResultUiState = SignUpResultUiState()
)

@Immutable
data class SignUpInfoUiState(
    val id: String = "",
    val password: String = "",
    val name: String = "",
    val email: String = "",
    val birthDay: String = "",
    val nickname: String = "",
    val imgFile: File? = null,
)

@Immutable
data class SignUpValidatedUiState(
    val userIdFormValidated: Boolean = false,
    val userIdDuplicatedUiState: DuplicatedUiState = DuplicatedUiState(),
    val passwordFormValidated: Boolean = false,
    val emailFormValidated: Boolean = false,
    val emailDuplicatedUiState: DuplicatedUiState = DuplicatedUiState(),
    val nicknameFormValidated: Boolean = false,
    val birthDayFormValidated: Boolean = false
)

@Immutable
data class SignUpTermUiState(
    val isEssential: Boolean = false,
    val description: Int = 0,
    val checkedStatus: CheckedStatus = CheckedStatus.UNCHECKED
)

@Immutable
data class DuplicatedUiState(
    val isSuccess: Boolean? = null,
    val duplicatedPass: Boolean = false
)

@Immutable
data class SignUpResultUiState(
    val isSuccess: Boolean? = null,
    val message: String = ""
)
