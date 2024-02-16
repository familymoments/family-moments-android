package io.familymoments.app.core.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import io.familymoments.app.core.component.LoadingIndicator
import io.familymoments.app.core.theme.FamilyMomentsTheme
import kotlin.reflect.KClass

abstract class BaseActivity<VM : BaseViewModel>(
    private val vmClass: KClass<VM>
) : ComponentActivity() {

    abstract val screen: @Composable () -> Unit

    protected val viewModel by lazy {
        ViewModelProvider(this, defaultViewModelProviderFactory)[vmClass.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FamilyMomentsTheme { AppContent { screen() } } }
    }

    @Composable
    fun AppContent(content: @Composable () -> Unit) {
        Box {
            content()
            LoadingIndicator(isLoading = viewModel.isLoading.collectAsState().value)
        }
    }
}
