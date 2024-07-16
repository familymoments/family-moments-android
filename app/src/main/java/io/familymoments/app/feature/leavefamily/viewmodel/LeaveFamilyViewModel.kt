package io.familymoments.app.feature.leavefamily.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.FamilyRepository
import io.familymoments.app.feature.leavefamily.uistate.LeaveFamilyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LeaveFamilyViewModel @Inject constructor(
    private val familyRepository: FamilyRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(LeaveFamilyUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkFamilyPermission()
    }

    private fun checkFamilyPermission() {
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                familyRepository.checkFamilyPermission(familyId)
            },
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    isOwner = it.result.isOwner
                )
            },
            onFailure = {}
        )
    }

    fun leaveFamily() {
        async(
            operation = {
                val familyId = userInfoPreferencesDataSource.loadFamilyId()
                familyRepository.leaveFamily(familyId)
            },
            onSuccess = {
                _uiState.value = _uiState.value.copy(isSuccess = true)
                userInfoPreferencesDataSource.removeFamilyId()
            },
            onFailure = {
                _uiState.value = _uiState.value.copy(
                    isSuccess = false,
                    errorMessage = it.message
                )
            }
        )
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = null)
    }
}
