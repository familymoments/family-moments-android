package io.familymoments.app.feature.login.ui.activity

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.core.base.BaseActivity
import io.familymoments.app.feature.login.ui.screen.LoginScreen
import io.familymoments.app.feature.login.viewmodel.LoginViewModel

@AndroidEntryPoint

class LoginActivity : BaseActivity<LoginViewModel>(LoginViewModel::class) {
    override val screen:   @Composable () -> Unit = { LoginScreen(viewModel) }
}
