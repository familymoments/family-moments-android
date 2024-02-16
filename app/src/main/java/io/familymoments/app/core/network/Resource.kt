package io.familymoments.app.core.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Fail(val exception: Throwable) : Resource<Nothing>()
}

// TODO Token Logic
suspend fun <T> execute(operation: suspend () -> T): Flow<Resource<T>> {
    return flow {
        try {
            emit(Resource.Loading)
            val response = operation()
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Fail(e))
        }
    }
}
