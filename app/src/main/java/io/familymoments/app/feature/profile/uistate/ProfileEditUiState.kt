package io.familymoments.app.feature.profile.uistate

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class ProfileEditUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val profileEditInfoUiState: ProfileEditInfoUiState = ProfileEditInfoUiState(),
    val profileEditValidated: ProfileEditValidated = ProfileEditValidated(),
    val profileImageUri: Uri
)

data class ProfileEditInfoUiState(
    val name: String = "",
    val nickname: String = "",
    val birthdate: String = ""
)

data class ProfileEditValidated(
    val nameValidated: Boolean = true,
    val nicknameValidated: Boolean = true,
    val birthdateValidated: Boolean = true
)
