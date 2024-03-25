package io.familymoments.app.feature.choosingfamily.activity

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.familymoments.app.core.base.BaseActivity
import io.familymoments.app.feature.choosingfamily.screen.ChoosingFamilyScreen
import io.familymoments.app.feature.creatingfamily.viewmodel.CreatingFamilyViewModel

@AndroidEntryPoint
class ChoosingFamilyActivity : BaseActivity<CreatingFamilyViewModel>(CreatingFamilyViewModel::class) {
    override val screen: @Composable () -> Unit = { ChoosingFamilyScreen(hiltViewModel(), hiltViewModel()) }
}
