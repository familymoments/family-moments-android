package io.familymoments.app.feature.mypage.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.feature.mypage.model.uistate.MyPageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel() {
    private val _myPageUiState: MutableStateFlow<MyPageUiState> = MutableStateFlow(MyPageUiState())
    val myPageUiState: StateFlow<MyPageUiState> = _myPageUiState.asStateFlow()

    fun logout() {
        async(
            operation = { userRepository.logoutUser() },
            onSuccess = {
                _myPageUiState.value = _myPageUiState.value.copy(
                    logoutUiState = _myPageUiState.value.logoutUiState.copy(
                        isSuccess = true
                    )
                )
            },
            onFailure = {
                _myPageUiState.value = _myPageUiState.value.copy(
                    logoutUiState = _myPageUiState.value.logoutUiState.copy(
                        isSuccess = false,
                        errorMessage = it.message
                    )
                )
            }
        )
    }
}
