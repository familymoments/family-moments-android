package io.familymoments.app.ui.bottomnav.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.familymoments.app.R
import io.familymoments.app.ui.bottomnav.BottomNavDestination
import io.familymoments.app.ui.bottomnav.model.BottomNavItem
import io.familymoments.app.ui.theme.AppColors
import io.familymoments.app.ui.theme.AppTypography.LB2_11

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = BottomNavItem.Home.route, Modifier.padding(innerPadding)) {
            composable(route = BottomNavDestination.Home.route) {
                // HomeScreen()
            }

            composable(route = BottomNavDestination.Album.route) {
                // AlbumScreen()
            }

            composable(route = BottomNavDestination.AddPost.route) {
                // AddPostScreen()
            }

            composable(route = BottomNavDestination.Calendar.route) {
                // CalendarScreen()
            }

            composable(route = BottomNavDestination.MyPage.route) {
                // MyPageScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Album,
        BottomNavItem.AddPost,
        BottomNavItem.Calendar,
        BottomNavItem.MyPage
    )

    BottomNavigation(
        modifier = Modifier
            .heightIn(min = 75.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        backgroundColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
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
                    if (item.route == BottomNavDestination.AddPost.route) {
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
