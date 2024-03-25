package io.familymoments.app.feature.choosingfamily.screen

import android.content.Intent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.FamilyMomentsTheme
import io.familymoments.app.feature.bottomnav.activity.MainActivity
import io.familymoments.app.feature.choosingfamily.route.ChoosingFamilyRoute
import io.familymoments.app.feature.creatingfamily.screen.SetAlarmScreen
import io.familymoments.app.feature.creatingfamily.screen.CopyInvitationLinkScreen
import io.familymoments.app.feature.creatingfamily.screen.SearchMemberScreen
import io.familymoments.app.feature.creatingfamily.screen.SetProfileScreen
import io.familymoments.app.feature.creatingfamily.viewmodel.CreatingFamilyViewModel
import io.familymoments.app.feature.joiningfamily.screen.JoinFamilyScreen
import io.familymoments.app.feature.joiningfamily.viewmodel.JoinFamilyViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ChoosingFamilyScreen(
    creatingFamilyViewModel: CreatingFamilyViewModel,
    joinFamilyViewModel: JoinFamilyViewModel
) {
    val context = LocalContext.current
    val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    AppBarScreen(title = {
        Text(
            text = stringResource(R.string.choosing_family_app_bar_screen_header),
            style = AppTypography.SH3_16,
            color = AppColors.deepPurple1
        )
    }) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = ChoosingFamilyRoute.Start.route) {
            composable(ChoosingFamilyRoute.Start.route) {
                StartScreen(
                    { navController.navigate(ChoosingFamilyRoute.SearchMember.route) },
                    { navController.navigate(ChoosingFamilyRoute.Join.route) },
                    {
                        context.startActivity(mainActivityIntent)
                    })
            }
            composable(ChoosingFamilyRoute.SearchMember.route) {
                SearchMemberScreen(
                    navigate = { navController.navigate(ChoosingFamilyRoute.SetProfile.route) },
                    viewModel = creatingFamilyViewModel
                )
            }
            composable(ChoosingFamilyRoute.SetProfile.route) {
                SetProfileScreen(viewModel = creatingFamilyViewModel) {
                    navController.navigate(ChoosingFamilyRoute.SetAlarm.route)
                }
            }
            composable(
                route = ChoosingFamilyRoute.SetAlarm.route
            ) {

                SetAlarmScreen(viewModel = creatingFamilyViewModel) {
                    val encodedUrl = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                    navController.navigate(ChoosingFamilyRoute.CopyInvitationLink.getRoute(encodedUrl))
                }
            }
            composable(
                route = ChoosingFamilyRoute.CopyInvitationLink.routeWithArgs,
                arguments = listOf(navArgument(ChoosingFamilyRoute.CopyInvitationLink.inviteLinkStringArgs) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                CopyInvitationLinkScreen(
                    backStackEntry.arguments?.getString(ChoosingFamilyRoute.CopyInvitationLink.inviteLinkStringArgs)
                        ?: ""
                ) {
                    context.startActivity(mainActivityIntent)
                }
            }
            composable(ChoosingFamilyRoute.Join.route) {
                JoinFamilyScreen(joinFamilyViewModel) {
                    context.startActivity(mainActivityIntent)
                }
            }
        }
    }

}

@Preview
@Composable
fun ChoosingFamilyScreenPreview() {
    FamilyMomentsTheme {
        ChoosingFamilyScreen(hiltViewModel(), hiltViewModel())
    }
}
