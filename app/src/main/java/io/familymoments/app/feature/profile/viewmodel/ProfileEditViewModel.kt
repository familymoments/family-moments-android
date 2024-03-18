package io.familymoments.app.feature.profile.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.profile.model.request.ProfileEditRequest
import io.familymoments.app.feature.profile.model.uistate.ProfileEditUiState
import io.familymoments.app.feature.profile.model.uistate.ProfileImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository,
    userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {
    private val name: String = checkNotNull(savedStateHandle[Route.ProfileEdit.nameArg])
    private val nickname: String = checkNotNull(savedStateHandle[Route.ProfileEdit.nicknameArg])
    private val birthdate: String = checkNotNull(savedStateHandle[Route.ProfileEdit.birthdateArg])
    private val profileImg: String = checkNotNull(savedStateHandle[Route.ProfileEdit.profileImgArg])

    private val _profileEditUiState: MutableStateFlow<ProfileEditUiState> = MutableStateFlow(
        ProfileEditUiState(
            profile = ProfileEditRequest(name, nickname, birthdate),
            profileImg = ProfileImage.Url(profileImg)
        )
    )
    val profileEditUiState: StateFlow<ProfileEditUiState> = _profileEditUiState.asStateFlow()
}
