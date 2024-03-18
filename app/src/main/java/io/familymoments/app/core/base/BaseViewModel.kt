package io.familymoments.app.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.network.AuthErrorManager
import io.familymoments.app.core.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var authErrorManager: AuthErrorManager

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun <T> async(
        operation: suspend () -> Flow<Resource<T>>,
        onSuccess: (T) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            operation().collect { result ->
                when (result) {
                    is Resource.Loading -> _isLoading.value = true
                    is Resource.Success -> {
                        _isLoading.value = false
                        onSuccess(result.data)
                    }

                    is Resource.Fail -> {
                        _isLoading.value = false
                        onFailure(result.exception)

                        if (result.exception.message == ERROR_403_MESSAGE || result.exception.message == ERROR_461_MESSAGE) {
                            authErrorManager.emitNeedReissueToken()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val ERROR_403_MESSAGE = "권한이 없는 유저의 접근입니다."
        private const val ERROR_461_MESSAGE = "Access Token의 기한이 만료되었습니다. 재발급 API를 호출해주세요."
    }
}
