package io.familymoments.app.feature.modifypassword.model.response

data class ModifyPasswordResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: String = ""
)
