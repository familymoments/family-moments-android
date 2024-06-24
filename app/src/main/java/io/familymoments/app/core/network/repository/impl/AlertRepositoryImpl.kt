package io.familymoments.app.core.network.repository.impl

import io.familymoments.app.core.network.Resource
import io.familymoments.app.core.network.api.AlertService
import io.familymoments.app.core.network.repository.AlertRepository
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val alertService: AlertService
) : AlertRepository {

    override suspend fun uploadAlarm() = flow {
        val response = alertService.uploadAlarm()
        if (response.isSuccessful) {
            emit(Resource.Success(response))
        } else {
            emit(Resource.Fail(Exception(response.message())))
        }
    }

}
