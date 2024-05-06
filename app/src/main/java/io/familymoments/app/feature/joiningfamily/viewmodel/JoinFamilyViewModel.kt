package io.familymoments.app.feature.joiningfamily.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.feature.joiningfamily.uistate.JoinFamilyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class JoinFamilyViewModel @Inject constructor(
    private val familyRepository: FamilyRepository
) : BaseViewModel() {

    private val _joinFamilyUiState: MutableStateFlow<JoinFamilyUiState> = MutableStateFlow(JoinFamilyUiState())
    val joinFamilyUiState: StateFlow<JoinFamilyUiState> = _joinFamilyUiState.asStateFlow()

    fun searchFamily(inviteLink: String) {
        async(
            operation = {
                familyRepository.searchFamilyByInviteLink(inviteLink)
            },
            onSuccess = { response ->
                _joinFamilyUiState.update {
                    it.copy(
                        searchFamilyByInviteLinkUiState = it.searchFamilyByInviteLinkUiState.copy(
                            isSuccess = true,
                            isLoading = isLoading.value,
                            result = response.result
                        )
                    )
                }
            },
            onFailure = { throwable ->
                _joinFamilyUiState.update {
                    it.copy(
                        searchFamilyByInviteLinkUiState = it.searchFamilyByInviteLinkUiState.copy(
                            isSuccess = false,
                            isLoading = isLoading.value,
                            errorMessage = throwable.message
                        )
                    )
                }
            }
        )
    }

    fun joinFamily() {
        val familyId = _joinFamilyUiState.value.selectedFamilyId ?: throw IllegalStateException()
        async(
            operation = {
                familyRepository.joinFamily(familyId)
            },
            onSuccess = { response ->
                _joinFamilyUiState.update {
                    it.copy(
                        joinFamilyExecuteUiState = it.joinFamilyExecuteUiState.copy(
                            isSuccess = true,
                            isLoading = isLoading.value,
                            result = response.result
                        )
                    )
                }
            },
            onFailure = { throwable ->
                _joinFamilyUiState.update {
                    it.copy(
                        joinFamilyExecuteUiState = it.joinFamilyExecuteUiState.copy(
                            isSuccess = false,
                            isLoading = isLoading.value,
                            errorMessage = throwable.message
                        )
                    )
                }
            }
        )
    }

    fun resetJoinFamilyExecuteSuccess() {
        _joinFamilyUiState.update {
            it.copy(
                joinFamilyExecuteUiState = it.joinFamilyExecuteUiState.copy(
                    isSuccess = null
                )
            )
        }
    }

    fun updateSelectedFamilyId(familyId: Long?) {
        _joinFamilyUiState.update {
            it.copy(selectedFamilyId = familyId)
        }
    }
}
