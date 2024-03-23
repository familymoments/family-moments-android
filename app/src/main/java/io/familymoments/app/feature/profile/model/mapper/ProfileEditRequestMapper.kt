package io.familymoments.app.feature.profile.model.mapper

import io.familymoments.app.feature.profile.model.request.ProfileEditRequest
import io.familymoments.app.feature.profile.model.uistate.ProfileEditInfoUiState

fun ProfileEditInfoUiState.toRequest() =
    ProfileEditRequest(
        name = name,
        nickname = nickname,
        birthdate = birthdate
    )
