package io.familymoments.app.feature.notification

import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.repository.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val alertRepository: AlertRepository
): BaseViewModel() {
    val stateText = MutableStateFlow("")

    fun uploadAlarm() {

        async(
            operation = { alertRepository.uploadAlarm() },
            onSuccess = {
                stateText.value = "Success"
            },
            onFailure = {
                stateText.value = "Fail"
            }
        )
    }
}
