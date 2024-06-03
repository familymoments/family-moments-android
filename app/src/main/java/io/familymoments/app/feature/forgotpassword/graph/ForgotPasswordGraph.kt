package io.familymoments.app.feature.forgotpassword.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.familymoments.app.feature.forgotpassword.screen.IdentifyScreen
import io.familymoments.app.feature.forgotpassword.screen.ResetScreen
import io.familymoments.app.feature.forgotpassword.screen.VerifyScreen
import io.familymoments.app.feature.forgotpassword.viewmodel.ForgotPasswordSharedViewModel
import androidx.hilt.navigation.compose.hiltViewModel as hiltViewModel1

fun NavGraphBuilder.forgotPasswordGraph(navController: NavController, forgotPasswordSharedViewModel: ForgotPasswordSharedViewModel) {
    composable(route = ForgotPasswordRoute.Identify.route) {
        IdentifyScreen(forgotPasswordSharedViewModel = forgotPasswordSharedViewModel, identifyViewModel = hiltViewModel1()) {
            navController.navigate(ForgotPasswordRoute.Verify.route)
        }
    }
    composable(route = ForgotPasswordRoute.Verify.route) {
        VerifyScreen(verifyViewModel = hiltViewModel1()) {
            navController.navigate(ForgotPasswordRoute.Reset.route)
        }
    }
    composable(route = ForgotPasswordRoute.Reset.route) {
        ResetScreen(forgotPasswordSharedViewModel = forgotPasswordSharedViewModel, resetViewModel = hiltViewModel1())
    }
}

sealed interface ForgotPasswordRoute {
    val route: String

    data object Identify : ForgotPasswordRoute {
        override val route: String = "Identify"
    }

    data object Verify : ForgotPasswordRoute {
        override val route: String = "Verify"
    }


    data object Reset : ForgotPasswordRoute {
        override val route: String = "Reset"
    }
}
