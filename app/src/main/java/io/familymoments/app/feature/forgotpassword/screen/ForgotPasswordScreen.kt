package io.familymoments.app.feature.forgotpassword.screen

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.forgotpassword.graph.ForgotPasswordRoute
import io.familymoments.app.feature.forgotpassword.graph.forgotPasswordGraph
import io.familymoments.app.feature.forgotpassword.viewmodel.ForgotPasswordSharedViewModel

@ExperimentalMaterial3Api
@Composable
fun ForgotPasswordScreen(forgotPasswordSharedViewModel: ForgotPasswordSharedViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    AppBarScreen(
        title = {
            Text(
                text = stringResource(id = R.string.forgot_password_title_01),
                style = AppTypography.SH3_16,
                color = AppColors.grey8
            )
        },
        navigationIcon = {
            Icon(
                modifier = Modifier.clickable {
                    (context as Activity).finish()
                },
                painter = painterResource(id = R.drawable.ic_app_bar_back),
                contentDescription = null,
                tint = Color.Unspecified
            )
        },
    ) {
        NavHost(navController = navController, startDestination = ForgotPasswordRoute.Identify.route) {
            forgotPasswordGraph(navController, forgotPasswordSharedViewModel)
        }
    }
}
