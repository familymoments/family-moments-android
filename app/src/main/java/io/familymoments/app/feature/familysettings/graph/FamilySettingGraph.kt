package io.familymoments.app.feature.familysettings.graph

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.util.scaffoldState
import io.familymoments.app.feature.familyinvitationlink.screen.FamilyInvitationLinkScreen
import io.familymoments.app.feature.familysettings.FamilySettingNavItem
import io.familymoments.app.feature.leavefamily.screen.LeaveFamilyScreen
import io.familymoments.app.feature.modifyfamilyInfo.screen.ModifyFamilyInfoScreen
import io.familymoments.app.feature.transferpermission.screen.TransferPermissionScreen
import io.familymoments.app.feature.removemember.screen.RemoveFamilyMemberScreen

fun NavGraphBuilder.familySettingGraph(navController: NavController) {
    composable(FamilySettingNavItem.ModifyFamilyInfo.route) {
        ModifyFamilyInfoScreen(
            modifier = Modifier.scaffoldState(hasShadow = false, hasBackButton = true),
            viewModel = hiltViewModel(),
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
    composable(FamilySettingNavItem.FamilyInvitationLink.route) {
        FamilyInvitationLinkScreen(
            modifier = Modifier.scaffoldState(hasShadow = false, hasBackButton = true),
            viewModel = hiltViewModel()
        )
    }
    composable (FamilySettingNavItem.AddFamilyMember.route) {
        // 가족 추가하기
    }
    composable (FamilySettingNavItem.ChangeUploadCycle.route) {
        // 업로드 주기 변경
    }
    composable (FamilySettingNavItem.TransferFamilyPermission.route) {
        TransferPermissionScreen(
            modifier = Modifier.scaffoldState(hasShadow = true, hasBackButton = true),
            viewModel = hiltViewModel(),
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
    composable (FamilySettingNavItem.RemoveFamilyMember.route) {
        RemoveFamilyMemberScreen(
            modifier = Modifier.scaffoldState(hasShadow = true, hasBackButton = true),
            viewModel = hiltViewModel(),
            navigateBack = {
                navController.popBackStack()
            },
            navigateToConfirmScreen = { userIds ->
                navController.navigate(Route.RemoveFamilyMember.getRoute(userIds))
            }
        )
    }
    composable(FamilySettingNavItem.LeaveFamily.route) {
        LeaveFamilyScreen(
            modifier = Modifier.scaffoldState(hasShadow = false, hasBackButton = true),
            viewModel = hiltViewModel(),
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
    composable(FamilySettingNavItem.DeleteFamily.route) {
        // 가족 삭제하기
    }
}

