package io.familymoments.app.feature.login.activity

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.core.base.BaseActivity
import io.familymoments.app.feature.login.screen.LoginScreen
import io.familymoments.app.feature.login.viewmodel.LoginViewModel
import timber.log.Timber

@AndroidEntryPoint

class LoginActivity : BaseActivity<LoginViewModel>(LoginViewModel::class) {
    override val screen: @Composable () -> Unit = { LoginScreen(viewModel) {
        callback.launch(it)
    } }

    @Suppress("DEPRECATION")
    private val callback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.result
            Timber.i("Google Login Success: ${account?.idToken}")
        }
    }
}
