package io.familymoments.app.feature.profile.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.feature.profile.model.uistate.ProfileViewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewViewModel @Inject constructor(
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {
    private val _profileViewUiState: MutableStateFlow<ProfileViewUiState> = MutableStateFlow(ProfileViewUiState())
    val profileViewUiState: StateFlow<ProfileViewUiState> = _profileViewUiState.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val userProfile = userInfoPreferencesDataSource.loadUserProfile()
                _profileViewUiState.value = _profileViewUiState.value.copy(
                    isSuccess = true,
                    userProfile = userProfile
                )
            } catch (e: Exception) {
                _profileViewUiState.value = _profileViewUiState.value.copy(
                    isSuccess = false,
                    errorMessage = e.message
                )
            }
        }
    }
}
