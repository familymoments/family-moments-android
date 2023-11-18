package io.familymoments.app.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.ui.screen.LoginScreen
import io.familymoments.app.viewmodel.LoginViewModel

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    val viewModel: LoginViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {

            LoginScreen(viewModel)
        }
    }
}