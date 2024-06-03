package io.familymoments.app.feature.familysettings.graph

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.familymoments.app.core.util.scaffoldState
import io.familymoments.app.feature.familyinvitationlink.screen.FamilyInvitationLinkScreen
import io.familymoments.app.feature.familysettings.FamilySettingNavItem

fun NavGraphBuilder.familySettingGraph(navController: NavController) {
    composable(FamilySettingNavItem.ModifyFamilyInfo.route) {
        // 가족 정보 수정
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
        // 가족 권한 넘기기
    }
    composable (FamilySettingNavItem.RemoveFamilyMember.route) {
        // 가족 강퇴시키기
    }
    composable(FamilySettingNavItem.LeaveFamily.route) {
        // 가족 탈퇴하기
    }
    composable(FamilySettingNavItem.DeleteFamily.route) {
        // 가족 삭제하기
    }
}

