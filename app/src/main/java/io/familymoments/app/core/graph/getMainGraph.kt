package io.familymoments.app.core.graph

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.familymoments.app.core.util.scaffoldState
import io.familymoments.app.feature.addpost.AddPostMode
import io.familymoments.app.feature.addpost.screen.AddPostScreen
import io.familymoments.app.feature.bottomnav.graph.bottomNavGraph
import io.familymoments.app.feature.calendar.screen.CalendarDayScreen
import io.familymoments.app.feature.familysettings.graph.familySettingGraph
import io.familymoments.app.feature.mypage.graph.myPageGraph
import io.familymoments.app.feature.postdetail.screen.PostDetailScreen
import io.familymoments.app.feature.profile.graph.profileGraph
import io.familymoments.app.feature.removemember.screen.RemoveFamilyMemberConfirmScreen

fun getMainGraph(
    navController: NavController
): NavGraphBuilder.() -> Unit = {

    bottomNavGraph(navController)
    profileGraph(navController)
    myPageGraph(navController)
    familySettingGraph(navController)

    composable(
        route = CommonRoute.POST_DETAIL.name + "/{postId}",
        arguments = listOf(navArgument("postId") { type = NavType.LongType })
    ) { backStackEntry ->
        PostDetailScreen(
            viewModel = hiltViewModel(),
            index = backStackEntry.arguments?.getLong("postId") ?: -1,
            modifier = Modifier
                .scaffoldState(
                    hasShadow = true,
                    hasBackButton = true,
                ),
            navigateToBack = {
                navController.popBackStack()
            }
        ) { post ->
            navController.navigate(
                Route.EditPost.getRoute(
                    mode = AddPostMode.EDIT.mode,
                    editPostId = post.postId,
                    editImages = post.imgs,
                    editContent = post.content
                )
            )
        }
    }

    composable(
        route = Route.EditPost.routeWithArgs,
        arguments = Route.EditPost.arguments
    ) {
        AddPostScreen(
            modifier = Modifier.scaffoldState(hasShadow = true, hasBackButton = true),
            viewModel = hiltViewModel(),
            popBackStack = navController::popBackStack
        )
    }

    composable(
        route = Route.CalendarDay.routeWithArs,
        arguments = Route.CalendarDay.arguments
    ) {
        CalendarDayScreen(
            modifier = Modifier.scaffoldState(
                hasShadow = true,
                hasBackButton = true,
            ),
            viewModel = hiltViewModel(),
            navigateToPostDetail = { postId ->
                navController.navigate(CommonRoute.POST_DETAIL.name + "/${postId}")
            },
            navigateToPostEdit = { post ->
                navController.navigate(
                    Route.EditPost.getRoute(
                        mode = AddPostMode.EDIT.mode,
                        editPostId = post.postId,
                        editImages = post.imgs,
                        editContent = post.content
                    )
                )
            }
        )
    }

    composable(
        route = Route.RemoveFamilyMember.routeWithArgs,
        arguments = Route.RemoveFamilyMember.arguments
    ) {
        RemoveFamilyMemberConfirmScreen(
            modifier = Modifier.scaffoldState(
                hasShadow = false,
                hasBackButton = true,
            ),
            viewModel = hiltViewModel(),
            navigateBack = {navController.popBackStack()}
        )
    }
}

enum class CommonRoute {
    POST_DETAIL
}
