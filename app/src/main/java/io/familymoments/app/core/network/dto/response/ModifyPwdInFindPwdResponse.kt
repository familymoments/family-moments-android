package io.familymoments.app.core.network.dto.response

data class ModifyPwdInFindPwdResponse(
    val isSuccess:Boolean = false,
    val code:Int = 0,
    val message:String = "",
    val result:String = ""
)
