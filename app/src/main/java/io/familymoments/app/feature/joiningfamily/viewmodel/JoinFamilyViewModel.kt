package io.familymoments.app.feature.joiningfamily.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.feature.joiningfamily.model.SearchFamilyByInviteLinkUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class JoinFamilyViewModel @Inject constructor(
    private val familyRepository: FamilyRepository
):BaseViewModel(){

    private val _searchFamilyByInviteLinkUiState:MutableStateFlow<SearchFamilyByInviteLinkUiState>
     = MutableStateFlow(SearchFamilyByInviteLinkUiState())
    val searchFamilyByInviteLinkUiState:StateFlow<SearchFamilyByInviteLinkUiState> = _searchFamilyByInviteLinkUiState.asStateFlow()

    fun searchFamilyByInviteLink(inviteLink:String){
        async(
            operation = {
                familyRepository.searchFamilyByInviteLink(inviteLink)
            },
            onSuccess = {
                _searchFamilyByInviteLinkUiState.value = _searchFamilyByInviteLinkUiState.value.copy(
                    isSuccess = true,
                    isLoading = isLoading.value,
                    result = it.result,
                )
            },
            onFailure = {
                _searchFamilyByInviteLinkUiState.value = _searchFamilyByInviteLinkUiState.value.copy(
                    isSuccess = false,
                    isLoading = isLoading.value,
                    errorMessage = it.message
                )
            }
        )
    }
}
