package io.familymoments.app.feature.bottomnav.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.AppTypography.LB2_11
import io.familymoments.app.feature.bottomnav.component.bottomNavShadow
import io.familymoments.app.feature.bottomnav.model.BottomNavItem
import io.familymoments.app.feature.home.screen.HomeScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val homeRoute = BottomNavItem.Home.route
    val albumRoute = BottomNavItem.Album.route
    val addPostRoute = BottomNavItem.AddPost.route
    val calendarRoute = BottomNavItem.Calendar.route
    val myPageRoute = BottomNavItem.MyPage.route

    val navigationIcon = @Composable {
        when (currentDestination?.route) {
            homeRoute, albumRoute -> {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(34.dp)
                        .clip(shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_sample_dog),
                        contentScale = ContentScale.Crop,
                        contentDescription = "profile"
                    )
                }
            }

            addPostRoute, calendarRoute, myPageRoute -> {
                Icon(
                    modifier = Modifier.padding(start = 12.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_app_bar_back),
                    contentDescription = null,
                    tint = AppColors.grey3
                )
            }
        }
    }

    val hasShadow =
        currentDestination?.route == homeRoute || currentDestination?.route == addPostRoute

    AppBarScreen(
        title = { Text(text = "sweety home", style = AppTypography.SH3_16, color = AppColors.deepPurple1) },
        navigationIcon = navigationIcon,
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentDestination = currentDestination
            )
        },
        hasShadow = hasShadow
    ) {
        NavHost(navController, startDestination = homeRoute) {
            composable(route = homeRoute) {
                HomeScreen()
            }

            composable(route = albumRoute) {
                // AlbumScreen()
            }

            composable(route = addPostRoute) {
                // AddPostScreen()
            }

            composable(route = calendarRoute) {
                // CalendarScreen()
            }

            composable(route = myPageRoute) {
                // MyPageScreen()
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Album,
        BottomNavItem.AddPost,
        BottomNavItem.Calendar,
        BottomNavItem.MyPage
    )

    BottomNavigation(
        modifier = Modifier
            .bottomNavShadow()
            .heightIn(min = 75.dp)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        backgroundColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.72.dp)
                ) {
                    if (item.route == BottomNavItem.AddPost.route) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = item.iconResId),
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = item.iconResId),
                            contentDescription = null,
                            tint = if (selected) AppColors.deepPurple1 else AppColors.grey3,
                        )
                        Text(
                            text = stringResource(id = item.labelResId),
                            style = LB2_11,
                            color = if (selected) AppColors.deepPurple1 else AppColors.grey3,
                            overflow = TextOverflow.Visible,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
