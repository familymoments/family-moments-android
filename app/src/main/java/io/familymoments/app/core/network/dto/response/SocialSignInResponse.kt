package io.familymoments.app.core.network.dto.response


data class SocialSignInResult(
    val isExisted: Boolean,
    val name: String? = "",
    val email: String? = "",
    val strBirthDate: String? = "",
    val nickname: String? = "",
    val picture: String? = "",
    val familyId: Long? = 0
)
