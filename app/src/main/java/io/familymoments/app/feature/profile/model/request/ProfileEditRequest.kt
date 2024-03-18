package io.familymoments.app.feature.profile.model.request

data class ProfileEditRequest(
    val name: String = "",
    val nickname: String = "",
    val birthdate: String = ""
)
