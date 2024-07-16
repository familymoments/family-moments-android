package io.familymoments.app.feature.forgotid.screen

import android.app.Activity
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.forgotid.graph.ForgotIdRoute
import io.familymoments.app.feature.forgotid.graph.forgotIdGraph

@Composable
fun ForgotIdScreen(goToLogin: () -> Unit, goToForgotPwd: () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    AppBarScreen(
        title = {
            Text(
                text = stringResource(id = R.string.login_forgot_id),
                style = AppTypography.SH3_16,
                color = AppColors.grey8
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!navController.popBackStack()) {
                        (context as Activity).finish()
                    }
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_app_bar_back),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                })
        }) {
        NavHost(navController = navController, startDestination = ForgotIdRoute.Verify.route) {
            forgotIdGraph(navController, goToLogin, goToForgotPwd)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ForgotIdScreenPreview() {
    ForgotIdScreen({}){}
}
