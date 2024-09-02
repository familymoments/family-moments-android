package io.familymoments.app.core.network.dto.response

data class UserProfile(
    val profileImg: String = "",
    val nickName: String = "",
    val email: String = "",
    val totalUpload: Int = 0,
    val duration: Int = 0
)
