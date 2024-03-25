package io.familymoments.app.feature.bottomnav.graph

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.familymoments.app.core.graph.CommonRoute
import io.familymoments.app.core.graph.Route
import io.familymoments.app.core.util.scaffoldState
import io.familymoments.app.feature.album.screen.AlbumScreen
import io.familymoments.app.feature.addpost.screen.AddPostScreen
import io.familymoments.app.feature.bottomnav.model.BottomNavItem
import io.familymoments.app.feature.calendar.screen.CalendarScreen
import io.familymoments.app.feature.home.screen.HomeScreen
import io.familymoments.app.feature.mypage.screen.MyPageScreen

fun NavGraphBuilder.bottomNavGraph(navController: NavController) {
    composable(route = BottomNavItem.Home.route) {
        HomeScreen(
            modifier = Modifier
                .scaffoldState(
                    hasShadow = true,
                    hasBackButton = false,
                ),
            viewModel = hiltViewModel(),
            navigateToPostDetail = {
                navController.navigate(CommonRoute.POST_DETAIL.name+ "/${it}")
            }
        )
    }

    composable(route = BottomNavItem.Album.route) {
        AlbumScreen(
            modifier = Modifier
                .scaffoldState(
                    hasShadow = false,
                    hasBackButton = false
                ),
            viewModel = hiltViewModel()
        )
    }

    composable(route = BottomNavItem.AddPost.route) {
        AddPostScreen(
            modifier = Modifier.scaffoldState(hasShadow = true, hasBackButton = true),
            viewModel = hiltViewModel(),
            navController::popBackStack
        )
    }

    composable(route = BottomNavItem.Calendar.route) {
        CalendarScreen(
            modifier = Modifier
                .scaffoldState(
                    hasShadow = false,
                    hasBackButton = true,
                ),
            viewModel = hiltViewModel(),
            navigateToCalendarDay = { localDateString ->
                navController.navigate(Route.CalendarDay.getRoute(localDateString))
            }
        )
    }

    composable(route = BottomNavItem.MyPage.route) {
        MyPageScreen(
            modifier = Modifier
                .scaffoldState(
                    hasShadow = false,
                    hasBackButton = true,
                ),
            onItemClick = { clickedItem ->
                navController.navigate(clickedItem.route)
            }
        )
    }
}
