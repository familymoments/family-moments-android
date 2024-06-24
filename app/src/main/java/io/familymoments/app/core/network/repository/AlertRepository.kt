package io.familymoments.app.core.network.repository

import io.familymoments.app.core.network.Resource
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    suspend fun uploadAlarm(): Flow<Resource<Any>>
}
