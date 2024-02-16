package io.familymoments.app.feature.bottomnav.activity

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.core.base.BaseActivity
import io.familymoments.app.feature.bottomnav.screen.MainScreen
import io.familymoments.app.feature.bottomnav.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class) {
    override val screen: @Composable () -> Unit = { MainScreen() }
}
