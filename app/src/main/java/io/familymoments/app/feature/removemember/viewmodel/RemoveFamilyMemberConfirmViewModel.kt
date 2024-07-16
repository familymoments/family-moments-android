package io.familymoments.app.feature.removemember.viewmodel

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.feature.removemember.uistate.RemoveFamilyMemberConfirmUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RemoveFamilyMemberConfirmViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val familyRepository: FamilyRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
): BaseViewModel() {
    private val userIds: Array<String> = checkNotNull(savedStateHandle[Route.RemoveFamilyMember.userIdsArg])

    private val _uiState = MutableStateFlow(RemoveFamilyMemberConfirmUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(userIds = getUserIdsList(userIds))
    }

    fun removeFamilyMember() {
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                familyRepository.removeFamilyMember(familyId, _uiState.value.userIds)
            },
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    isSuccess = true
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

    private fun getUserIdsList(userIds: Array<String>): List<String> {
        val regex = Regex("[\\[\\] ]")
        return userIds.getOrNull(0)?.replace(regex, "")?.split(",") ?: listOf()
    }
}
