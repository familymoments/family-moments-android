package io.familymoments.app.feature.forgotpassword.viewmodel

import io.familymoments.app.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgotPasswordSharedViewModel:BaseViewModel() {
    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    fun updateId(id: String) {
        _id.value = id
    }
}
