package io.familymoments.app.feature.profile.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.profile.model.mapper.toRequest
import io.familymoments.app.feature.profile.model.uistate.ProfileEditInfoUiState
import io.familymoments.app.feature.profile.model.uistate.ProfileEditUiState
import io.familymoments.app.feature.profile.model.uistate.ProfileImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val name: String = checkNotNull(savedStateHandle[Route.ProfileEdit.nameArg])
    private val nickname: String = checkNotNull(savedStateHandle[Route.ProfileEdit.nicknameArg])
    private val birthdate: String = checkNotNull(savedStateHandle[Route.ProfileEdit.birthdateArg])
    private val profileImg: String = checkNotNull(savedStateHandle[Route.ProfileEdit.profileImgArg])

    private val _profileEditUiState: MutableStateFlow<ProfileEditUiState> = MutableStateFlow(
        ProfileEditUiState(
            profileEditInfoUiState = ProfileEditInfoUiState(name, nickname, birthdate),
            profileImage = ProfileImage.Url(profileImg)
        )
    )
    val profileEditUiState: StateFlow<ProfileEditUiState> = _profileEditUiState.asStateFlow()

    fun imageChanged(bitmap: Bitmap?) {
        if (bitmap == null) return
        _profileEditUiState.value = _profileEditUiState.value.copy(
            profileImage = ProfileImage.Bitmap(bitmap)
        )
    }

    fun nameChanged(name: String) {
        _profileEditUiState.value = _profileEditUiState.value.copy(
            profileEditInfoUiState = profileEditUiState.value.profileEditInfoUiState.copy(name = name)
        )
    }

    fun nicknameChanged(nickname: String) {
        _profileEditUiState.value = _profileEditUiState.value.copy(
            profileEditInfoUiState = profileEditUiState.value.profileEditInfoUiState.copy(nickname = nickname)
        )
    }

    fun birthdateChanged(birthdate: String) {
        _profileEditUiState.value = _profileEditUiState.value.copy(
            profileEditInfoUiState = profileEditUiState.value.profileEditInfoUiState.copy(birthdate = birthdate)
        )
    }

    fun editUserProfile(imageFile: File) {
        val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val profileImgPart = MultipartBody.Part.createFormData("profileImg", imageFile.name, imageRequestBody)
        async(
            operation = {
                userRepository.editUserProfile(
                    profileEditRequest = _profileEditUiState.value.profileEditInfoUiState.toRequest(),
                    profileImg = profileImgPart
                )
            },
            onSuccess = {
                _profileEditUiState.value = _profileEditUiState.value.copy(isSuccess = true)
            },
            onFailure = {
                _profileEditUiState.value = _profileEditUiState.value.copy(
                    isSuccess = false,
                    errorMessage = it.message
                )
            }
        )
    }
}
