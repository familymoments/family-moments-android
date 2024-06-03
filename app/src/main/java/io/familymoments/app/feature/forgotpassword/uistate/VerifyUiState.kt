package io.familymoments.app.feature.forgotpassword.uistate

data class VerifyUiState(
    val sendEmailUiState: SendEmailUiState = SendEmailUiState(),
    val findPwdUiState: FindPwdUiState = FindPwdUiState()
)

data class SendEmailUiState(
    val isSuccess: Boolean? = null,
    val isLoading:Boolean = false,
    val result:String = "",
    val message: String = ""
)

data class FindPwdUiState(
    val isSuccess: Boolean? = null,
    val result:String = "",
    val message: String = ""
)
