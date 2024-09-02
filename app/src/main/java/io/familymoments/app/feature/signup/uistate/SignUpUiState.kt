package io.familymoments.app.feature.signup.uistate

import androidx.compose.runtime.Immutable
import io.familymoments.app.core.component.CheckedStatus
import java.io.File

@Immutable
data class SignUpUiState(
    val isLoading: Boolean = false,
    val postSuccess:Boolean? = null,
    val signUpSuccess:Boolean? = null,
    val message:String = "",
    val signUpInfoUiState: SignUpInfoUiState = SignUpInfoUiState(),
    val signUpValidatedUiState: SignUpValidatedUiState = SignUpValidatedUiState(),
    val signUpTermUiState: SignUpTermUiState = SignUpTermUiState(),
    val verificationCodeButtonUiState: VerificationCodeButtonUiState = VerificationCodeButtonUiState(),
    val expirationTimeUiState: ExpirationTimeUiState = ExpirationTimeUiState(),
)

@Immutable
data class SignUpInfoUiState(
    val id: String = "",
    val password: String = "",
    val email: String = "",
    val nickname: String = "",
    val imgFile: File? = null,
)

@Immutable
data class SignUpValidatedUiState(
    val userIdFormValidated: Boolean = false,
    val userIdDuplicatedPass: Boolean = false,
    val passwordFormValidated: Boolean = false,
    val passwordSameCheck:Boolean = false,
    val emailFormValidated: Boolean = false,
    val emailVerified:Boolean = false,
    val nicknameFormValidated: Boolean = false,
)

@Immutable
data class SignUpTermUiState(
    val isEssential: Boolean = false,
    val description: Int = 0,
    val checkedStatus: CheckedStatus = CheckedStatus.UNCHECKED,
    val url: String = ""
)

@Immutable
data class VerificationCodeButtonUiState(
    val sendEmailAvailable: Boolean = true,
    val verifyCodeAvailable: Boolean = true,
)

@Immutable
data class ExpirationTimeUiState(
    val expirationTime: Int = 0,
    val isExpirationTimeVisible: Boolean = false
)
