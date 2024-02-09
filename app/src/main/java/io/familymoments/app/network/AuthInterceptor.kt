package io.familymoments.app.network

import android.util.Log
import io.familymoments.app.TAG
import io.familymoments.app.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenRepository.loadAccessToken()
        }
        Log.d(TAG, "intercept: $token")
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, token)
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION = "X-AUTH-TOKEN"
    }
}
