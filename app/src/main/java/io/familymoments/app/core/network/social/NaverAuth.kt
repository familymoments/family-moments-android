package io.familymoments.app.core.network.social

import android.content.Context
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import timber.log.Timber

object NaverAuth {
    fun login(context: Context, callback: (String?) -> Unit = {}) {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                callback(NaverIdLoginSDK.getAccessToken())
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Timber.e("errorCode: $errorCode, errorDescription: $errorDescription")
                callback(null)
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
                callback(null)
            }
        }

        NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
    }

    fun naverLogout() {
        NaverIdLoginSDK.logout()
    }

    fun naverUnlink(callback: (Throwable?) -> Unit) {
        NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
            override fun onSuccess() {
                callback(null)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                callback(Throwable("errorCode: $errorCode, errorDescription: $errorDescription"))
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }
}
