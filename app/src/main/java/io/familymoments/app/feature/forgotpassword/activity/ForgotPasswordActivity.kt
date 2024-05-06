package io.familymoments.app.feature.forgotpassword.activity

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.core.base.BaseActivity
import io.familymoments.app.feature.forgotpassword.screen.ForgotPasswordScreen
import io.familymoments.app.feature.forgotpassword.viewmodel.IdentifyViewModel

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity<IdentifyViewModel>(IdentifyViewModel::class) {
    @OptIn(ExperimentalMaterial3Api::class)
    override val screen: @Composable () -> Unit = { ForgotPasswordScreen(viewModel) }
}
