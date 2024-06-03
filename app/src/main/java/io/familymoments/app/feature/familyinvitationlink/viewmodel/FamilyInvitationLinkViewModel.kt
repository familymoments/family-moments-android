package io.familymoments.app.feature.familyinvitationlink.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.feature.familyinvitationlink.uistate.FamilyInvitationLinkUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FamilyInvitationLinkViewModel @Inject constructor(
    private val familyRepository: FamilyRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
): BaseViewModel() {
    private val _uiState: MutableStateFlow<FamilyInvitationLinkUiState> = MutableStateFlow(FamilyInvitationLinkUiState())
    val uiState: StateFlow<FamilyInvitationLinkUiState> = _uiState.asStateFlow()

    fun getFamilyInvitationLink() {
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                familyRepository.getFamilyInfo(familyId)
            },
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    isSuccess = true,
                    invitationLink = it.inviteCode
                )
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
