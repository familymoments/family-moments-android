package io.familymoments.app.feature.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.network.dto.request.ProfileEditRequest
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.profile.uistate.ProfileEditInfoUiState
import io.familymoments.app.feature.profile.uistate.ProfileEditUiState
import io.familymoments.app.feature.signup.UserInfoFormatChecker.checkBirthDay
import io.familymoments.app.feature.signup.UserInfoFormatChecker.checkNickname
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

    private val _uiState: MutableStateFlow<ProfileEditUiState> = MutableStateFlow(
        ProfileEditUiState(
            profileEditInfoUiState = ProfileEditInfoUiState(name, nickname, birthdate),
            profileImageUri = Uri.parse(profileImg)
        )
    )
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    fun imageChanged(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            profileImageUri = uri
        )
    }

    fun validateName(name: String) {
        _uiState.value = _uiState.value.copy(
            profileEditValidated = _uiState.value.profileEditValidated.copy(nameValidated = name.isNotEmpty())
        )
    }

    fun validateNickname(nickname: String) {
        _uiState.value = _uiState.value.copy(
            profileEditValidated = _uiState.value.profileEditValidated.copy(nicknameValidated = checkNickname(nickname))
        )
    }

    fun validateBirthdate(birthdate: String) {
        _uiState.value = _uiState.value.copy(
            profileEditValidated = _uiState.value.profileEditValidated.copy(birthdateValidated = checkBirthDay(birthdate))
        )
    }

    fun editUserProfile(imageFile: File, name: String, nickname: String, birthdate: String) {
        val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val profileImgPart = MultipartBody.Part.createFormData("profileImg", imageFile.name, imageRequestBody)
        async(
            operation = {
                userRepository.editUserProfile(
                    profileEditRequest = ProfileEditRequest(name, nickname, birthdate),
                    profileImg = profileImgPart
                )
            },
            onSuccess = {
                _uiState.value = _uiState.value.copy(isSuccess = true)
            },
            onFailure = {
                _uiState.value = _uiState.value.copy(
                    isSuccess = false,
                    errorMessage = it.message
                )
            }
        )
    }
}
