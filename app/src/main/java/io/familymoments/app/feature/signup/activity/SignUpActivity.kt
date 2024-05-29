package io.familymoments.app.feature.signup.activity

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.core.base.BaseActivity
import io.familymoments.app.feature.signup.screen.SignUpScreen
import io.familymoments.app.feature.signup.viewmodel.SignUpViewModel

@AndroidEntryPoint
class SignUpActivity : BaseActivity<SignUpViewModel>(SignUpViewModel::class) {
    override val screen: @Composable () -> Unit = {
        SignUpScreen(viewModel)
    }
}
