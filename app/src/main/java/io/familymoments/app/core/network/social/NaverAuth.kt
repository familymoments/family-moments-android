package io.familymoments.app.core.network.social

import android.content.Context
import android.widget.Toast
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

object NaverAuth {
    fun login(context: Context, callback: (String?) -> Unit = {}) {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
//                binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
//                binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
//                binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
//                binding.tvType.text = NaverIdLoginSDK.getTokenType()
//                binding.tvState.text = NaverIdLoginSDK.getState().toString()
                callback(NaverIdLoginSDK.getAccessToken())
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(context, "errorCode: $errorCode, errorDescription: $errorDescription", Toast.LENGTH_SHORT).show()
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
}
