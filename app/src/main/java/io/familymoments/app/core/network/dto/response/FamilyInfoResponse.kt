package io.familymoments.app.core.network.dto.response

data class FamilyInfoResponse(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: FamilyInfo = FamilyInfo()
)

data class FamilyInfo(
    val owner: String = "",
    val familyName: String = "",
    val uploadCycle: Int? = null,
    val inviteCode: String = "",
    val representImg: String = ""
)
