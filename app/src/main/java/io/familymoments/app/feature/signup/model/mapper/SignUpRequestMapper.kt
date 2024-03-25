package io.familymoments.app.feature.signup.model.mapper

import io.familymoments.app.feature.signup.model.request.SignUpRequest
import io.familymoments.app.feature.signup.model.uistate.SignUpInfoUiState

fun SignUpInfoUiState.toRequest() =
    SignUpRequest(
        id = id,
        passwordA = password,
        passwordB = password,
        name = name,
        email = email,
        strBirthDate = birthDay,
        nickname = nickname
    )
