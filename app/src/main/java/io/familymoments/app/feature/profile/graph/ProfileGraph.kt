package io.familymoments.app.feature.profile.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.familymoments.app.feature.profile.screen.ProfileEditScreen
import io.familymoments.app.feature.profile.screen.ProfileViewScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(route = ProfileScreenRoute.View.name) {
        ProfileViewScreen(
            navigateToProfileEdit = {
                navController.navigate(ProfileScreenRoute.Edit.name)
            },
            viewModel = hiltViewModel()
        )
    }
    composable(route = ProfileScreenRoute.Edit.name) {
        ProfileEditScreen(
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
}

enum class ProfileScreenRoute {
    View,
    Edit
}
