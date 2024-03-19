package io.familymoments.app.feature.profile.model.uistate

import io.familymoments.app.feature.profile.model.request.ProfileEditRequest

data class ProfileEditUiState(
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val profile: ProfileEditRequest = ProfileEditRequest(),
    val profileImage: ProfileImage
)

sealed class ProfileImage {
    data class Url(val imgUrl: String) : ProfileImage()
    data class Bitmap(val bitmap: android.graphics.Bitmap) : ProfileImage()
}

