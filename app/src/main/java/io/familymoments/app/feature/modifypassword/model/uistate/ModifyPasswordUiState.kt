package io.familymoments.app.feature.modifypassword.model.uistate

data class ModifyPasswordUiState(
    var password: String = "",
    var newPassword: String = "",
    var newPasswordCheck: String = ""
)
