package io.familymoments.app.core.network

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Fail(val exception: Throwable) : Resource<Nothing>()
}
