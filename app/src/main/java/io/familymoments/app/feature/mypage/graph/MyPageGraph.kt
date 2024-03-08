package io.familymoments.app.feature.mypage.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.familymoments.app.feature.modifypassword.screen.ModifyPasswordScreen
import io.familymoments.app.feature.profile.graph.profileGraph

fun NavGraphBuilder.myPageGraph(navController: NavController){
    composable(route = MyPageRoute.Profile.name) {
        profileGraph(navController)
    }
    composable(route = MyPageRoute.Password.name) {
        ModifyPasswordScreen(viewModel = hiltViewModel())
    }
    composable(route = MyPageRoute.Notification.name) {
        // Notification Screen
    }
    composable(route = MyPageRoute.FamilyInvitationList.name) {
        // FamilyInvitationList Screen
    }
    composable(route = MyPageRoute.Notification.name) {
        // Notification Screen
    }
    composable(route = MyPageRoute.FamilySettings.name) {
        // FamilySettings Screen
    }
    composable(route = MyPageRoute.Logout.name) {
        // Logout Screen
    }
    composable(route = MyPageRoute.AccountDeletion.name) {
        // AccountDeletion Screen
    }
}

enum class MyPageRoute{
    Profile,
    Password,
    Notification,
    FamilyInvitationList,
    FamilySettings,
    Logout,
    AccountDeletion,
}