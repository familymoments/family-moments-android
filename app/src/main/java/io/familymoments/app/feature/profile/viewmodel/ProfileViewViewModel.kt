package io.familymoments.app.feature.profile.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.profile.model.uistate.ProfileViewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewViewModel @Inject constructor(
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource,
    private val userRepository: UserRepository
) : BaseViewModel() {
    private val _profileViewUiState: MutableStateFlow<ProfileViewUiState> = MutableStateFlow(ProfileViewUiState())
    val profileViewUiState: StateFlow<ProfileViewUiState> = _profileViewUiState.asStateFlow()

    fun loadUserProfile() {
        async(
            operation = {
                val loadedFamilyId = userInfoPreferencesDataSource.loadFamilyId()
                val familyId = if (loadedFamilyId == NULL_FAMILY_ID) null else loadedFamilyId
                userRepository.loadUserProfile(familyId)
            },
            onSuccess = {
                _profileViewUiState.value = _profileViewUiState.value.copy(
                    isSuccess = true,
                    userProfile = it.result
                )
            },
            onFailure = {
                _profileViewUiState.value = _profileViewUiState.value.copy(
                    isSuccess = false,
                    errorMessage = it.message
                )
            }
        )
    }

    companion object {
        const val NULL_FAMILY_ID = -1L
    }
}
