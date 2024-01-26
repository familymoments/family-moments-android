package io.familymoments.app.ui.bottomnav.ui.activity

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.ui.activity.BaseActivity
import io.familymoments.app.ui.bottomnav.ui.screen.MainScreen
import io.familymoments.app.ui.bottomnav.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class) {
    override val screen: @Composable () -> Unit = { MainScreen() }
}
