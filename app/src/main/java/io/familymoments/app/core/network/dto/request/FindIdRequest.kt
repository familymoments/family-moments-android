package io.familymoments.app.core.network.dto.request

data class FindIdRequest(
    val name:String,
    val email:String,
    val code:String
)
