package io.familymoments.app

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.familymoments.app.feature.bottomnav.model.BottomNavItem
import io.familymoments.app.feature.postdetail.screen.PostDetailScreen

fun getMainGraph(
    navController: NavController
): NavGraphBuilder.() -> Unit = {

    bottomNavGraph(navController)

    composable(route = "PostDetail") {
        PostDetailScreen(
            modifier = Modifier
                .scaffoldState(
                    hasShadow = true,
                    hasBackButton = true,
                    selectedBottomNav = BottomNavItem.Home
                )
        )
    }
}
