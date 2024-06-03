package io.familymoments.app.feature.mypage.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import io.familymoments.app.core.network.repository.UserRepository
import io.familymoments.app.core.network.social.KakaoAuth
import io.familymoments.app.core.network.social.KakaoAuth.kakaoLogout
import io.familymoments.app.core.network.social.NaverAuth
import io.familymoments.app.core.network.social.NaverAuth.naverLogout
import io.familymoments.app.feature.mypage.uistate.MyPageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : BaseViewModel() {
    private val _myPageUiState: MutableStateFlow<MyPageUiState> = MutableStateFlow(MyPageUiState())
    val myPageUiState: StateFlow<MyPageUiState> = _myPageUiState.asStateFlow()

    fun logout() {
        async(
            operation = { userRepository.logoutUser() },
            onSuccess = {

                val type = userInfoPreferencesDataSource.loadSocialLoginType()
                println(type)
                when (type) {
                    KakaoAuth.NAME -> kakaoLogout()
                    NaverAuth.NAME -> naverLogout()
                }

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
