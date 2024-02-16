package io.familymoments.app.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.familymoments.app.core.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract  class BaseViewModel : ViewModel() {

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
                    }
                }
            }
        }
    }


}
