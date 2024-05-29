package io.familymoments.app.feature.signup.activity

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.core.base.BaseActivity
import io.familymoments.app.core.network.dto.response.LoginResult
import io.familymoments.app.feature.signup.screen.SocialSignUpScreen
import io.familymoments.app.feature.signup.viewmodel.SignUpViewModel

@AndroidEntryPoint
class SocialSignUpActivity : BaseActivity<SignUpViewModel>(SignUpViewModel::class) {
    override val screen: @Composable () -> Unit = {
        val socialType = intent.getStringExtra("socialType") ?: ""
        val socialToken = intent.getStringExtra("socialToken") ?: ""

        val socialLoginResult = LoginResult(
            email = intent.getStringExtra("email") ?: "",
            name = intent.getStringExtra("name") ?: "",
            nickname = intent.getStringExtra("nickname") ?: "",
            strBirthDate = intent.getStringExtra("strBirthDate") ?: ""
        )

        SocialSignUpScreen(
            viewModel,
            socialType,
            socialToken,
            socialLoginResult
        ) {

        }
    }
}
