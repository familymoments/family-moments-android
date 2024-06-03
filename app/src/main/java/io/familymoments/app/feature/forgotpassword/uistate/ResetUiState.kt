package io.familymoments.app.feature.forgotpassword.uistate

data class ResetUiState(
    val password:String = "",
    val passwordConfirm:String = "",
    val isValid:Boolean = false,
    val isSuccess: Boolean? = null,
    val isLoading:Boolean = false,
    val result:String = "",
    val message: String = "",
    val showDialog:Boolean = false
)
