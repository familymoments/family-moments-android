package io.familymoments.app.feature.splash.viewmodel

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.core.base.BaseViewModel
import io.familymoments.app.core.network.HttpResponseMessage.FAMILY_NOT_EXIST_404
import io.familymoments.app.core.network.HttpResponseMessage.USER_NOT_IN_FAMILY_404
import io.familymoments.app.core.network.repository.FamilyRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val familyRepository: FamilyRepository
) : BaseViewModel() {

    enum class NextRoute {
        INIT,
        LOGIN,
        MAIN,
        CHOOSING_FAMILY
    }

    val currentRoute = MutableLiveData(NextRoute.INIT)

    suspend fun autoSignIn() {
        delay(2000)
        async(
            operation = {
                familyRepository.getFamilyName()
            },
            onSuccess = {
                if (it.isSuccess) {
                    currentRoute.value = NextRoute.MAIN
                }
            },
            onFailure = {
                if (it.message == FAMILY_NOT_EXIST_404 || it.message == USER_NOT_IN_FAMILY_404) {
                    currentRoute.value = NextRoute.CHOOSING_FAMILY
                } else {
                    currentRoute.value = NextRoute.LOGIN
                }
            }
        )
    }
}
