package io.familymoments.app.feature.modifypassword.model.request

data class ModifyPasswordRequest(
    var password: String = "",
    var newPassword_first: String = "",
    var newPassword: String = ""
)
