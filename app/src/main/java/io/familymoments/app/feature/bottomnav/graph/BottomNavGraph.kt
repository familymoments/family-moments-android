package io.familymoments.app.feature.bottomnav.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.familymoments.app.core.util.scaffoldState
import io.familymoments.app.feature.bottomnav.model.BottomNavItem
import io.familymoments.app.feature.home.screen.HomeScreen

fun NavGraphBuilder.bottomNavGraph(navController: NavController) {
    composable(route = BottomNavItem.Home.route) {
        HomeScreen(
            modifier = Modifier
                .scaffoldState(
                    hasShadow = true,
                    hasBackButton = false,
                    selectedBottomNav = BottomNavItem.Home
                ),
            navigateToPostDetail = {
                navController.navigate("PostDetail")
            }
        )
    }

    composable(route = BottomNavItem.Album.route) {
        // AlbumScreen()
    }

    composable(route = BottomNavItem.AddPost.route) {
        // AddPostScreen()
    }

    composable(route = BottomNavItem.Calendar.route) {
        // CalendarScreen()
    }

    composable(route = BottomNavItem.MyPage.route) {
        // MyPageScreen()
    }
}
