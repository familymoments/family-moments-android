package io.familymoments.app.feature.profile.model.response

data class ProfileEditResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: ProfileEditResult = ProfileEditResult()
)

data class ProfileEditResult(
    val name: String = "",
    val nickname: String = "",
    val birthdate: String = "",
    val profileImg: String = ""
)
