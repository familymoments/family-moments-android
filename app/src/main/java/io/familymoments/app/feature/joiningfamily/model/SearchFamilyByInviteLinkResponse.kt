package io.familymoments.app.feature.joiningfamily.model

data class SearchFamilyByInviteLinkResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: SearchFamilyByInviteLinkResult? = null
)

data class SearchFamilyByInviteLinkResult(
    val owner: String = "",
    val familyName: String = "",
    val uploadCycle: Int? = null,
    val inviteCode: String = "",
    val representImg: String = ""
)
