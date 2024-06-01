package io.familymoments.app.feature.signup.mapper

import io.familymoments.app.core.network.dto.request.SignUpRequest
import io.familymoments.app.core.network.dto.request.UserJoinReq
import io.familymoments.app.feature.signup.uistate.SignUpInfoUiState

fun SignUpInfoUiState.toRequest() =
    SignUpRequest(
        id = id,
        password = password,
        name = name,
        email = email,
        strBirthDate = birthDay,
        nickname = nickname
    )

fun SignUpInfoUiState.toUserJoinReq(socialType: String): UserJoinReq {
    return UserJoinReq(
        id = id,
        name = name,
        email = email,
        strBirthDate = birthDay,
        nickname = nickname,
        userType = socialType
    )
}
