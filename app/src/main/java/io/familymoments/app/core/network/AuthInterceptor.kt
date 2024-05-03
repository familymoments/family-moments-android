package io.familymoments.app.core.network

import io.familymoments.app.core.network.datasource.UserInfoPreferencesDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userInfoPreferencesDataSource: UserInfoPreferencesDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            userInfoPreferencesDataSource.loadAccessToken()
        }
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, token)
            .build()

        request.headers.forEach { header ->
            Timber.i("header: $header")
        }

        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION = "X-AUTH-TOKEN"
    }
}
