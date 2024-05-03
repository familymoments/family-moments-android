package io.familymoments.app.core.network.dto.response

import io.familymoments.app.core.network.HttpResponse
import io.familymoments.app.core.network.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

data class ApiResponse<T>(
    val isSuccess: Boolean = false,
    val code: Int = 0,
    val message: String = "",
    val result: T
)

fun <T> getResourceFlow(
    response: Response<ApiResponse<T>>,
) = flow {
    emit(Resource.Loading)
    emit(getHttpResult(response))
}.catch { e ->
    emit(Resource.Fail(e))
}

fun <T> getHttpResult(
    response: Response<ApiResponse<T>>,
    code: Int = -1,
    errorFunction: () -> Resource<ApiResponse<T>> = {
        Resource.Fail(Throwable(response.message()))
    }
): Resource<ApiResponse<T>> {
    val responseBody = response.body()
    if (response.code() == HttpResponse.SUCCESS) {
        return if (responseBody != null && responseBody.isSuccess) {
            Resource.Success(responseBody)
        } else {
            Resource.Fail(Throwable(responseBody?.message))
        }
    } else {
        if (code != -1) {
            return errorFunction()
        }
    }
    return Resource.Fail(Throwable(response.message()))
}
