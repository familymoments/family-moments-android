package io.familymoments.app.feature.profile.uistate

import androidx.compose.runtime.Immutable
import java.io.File

@Immutable
data class ProfileEditUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val profileEditInfoUiState: ProfileEditInfoUiState = ProfileEditInfoUiState(),
    val profileEditValidated: ProfileEditValidated = ProfileEditValidated(),
    val profileImage: File? = null
)

data class ProfileEditInfoUiState(
    val nickname: String = "",
)

data class ProfileEditValidated(
    val nicknameValidated: Boolean = true,
)
