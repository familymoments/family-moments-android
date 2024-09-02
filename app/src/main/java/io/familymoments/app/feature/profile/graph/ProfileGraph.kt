package io.familymoments.app.feature.profile.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.familymoments.app.core.graph.Route
import io.familymoments.app.feature.profile.screen.ProfileEditScreen
import io.familymoments.app.feature.profile.screen.ProfileViewScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(route = ProfileScreenRoute.View.name) {
        ProfileViewScreen(
            navigateToProfileEdit = { userProfile ->
                navController.navigate(
                    Route.ProfileEdit.getRoute(
                        nickname = userProfile.nickName,
                        profileImg = userProfile.profileImg
                    )
                )
            },
            viewModel = hiltViewModel()
        )
    }
    composable(
        route = Route.ProfileEdit.routeWithArgs,
        arguments = Route.ProfileEdit.arguments
    ) {
        ProfileEditScreen(
            navigateBack = {
                navController.popBackStack()
            },
            viewModel = hiltViewModel()
        )
    }
}

enum class ProfileScreenRoute {
    View,
    Edit
}
