package io.familymoments.app.feature.login.activity

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.core.base.BaseActivity
import io.familymoments.app.feature.bottomnav.activity.MainActivity
import io.familymoments.app.feature.forgotid.activity.ForgotIdActivity
import io.familymoments.app.feature.forgotpassword.activity.ForgotPasswordActivity
import io.familymoments.app.feature.login.screen.LoginScreen
import io.familymoments.app.feature.login.uistate.LoginUiState
import io.familymoments.app.feature.login.viewmodel.LoginViewModel
import io.familymoments.app.feature.signup.activity.SignUpActivity
import io.familymoments.app.feature.signup.activity.SocialSignUpActivity

@AndroidEntryPoint

class LoginActivity : BaseActivity<LoginViewModel>(LoginViewModel::class) {
    override val screen: @Composable () -> Unit = {
        LoginScreen(viewModel, ::routeToSignUp, ::routeToMainActivity, ::routeToForgotPassword, ::routeToForgotId)
    }

    private val socialSignUpActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_CANCELED) {
            viewModel.logout()
        }
    }


    private fun routeToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun routeToSignUp(loginUiState: LoginUiState) {
        val socialType = loginUiState.socialType
        if (socialType.isEmpty()) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, SocialSignUpActivity::class.java)
            intent.putExtra("socialType", socialType)
            intent.putExtra("socialToken", loginUiState.socialToken)
            intent.putExtra("email", loginUiState.loginResult?.email)
            intent.putExtra("nickname", loginUiState.loginResult?.nickname)
            socialSignUpActivity.launch(intent)
        }
    }

    private fun routeToForgotPassword() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    private fun routeToForgotId() {
        startActivity(Intent(this, ForgotIdActivity::class.java))
    }

}
