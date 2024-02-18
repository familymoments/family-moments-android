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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.familymoments.app.core.util.LocalScaffoldState
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.AppTypography.LB2_11
import io.familymoments.app.feature.bottomnav.component.bottomNavShadow
import io.familymoments.app.feature.bottomnav.model.BottomNavItem
import io.familymoments.app.core.graph.getMainGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val scaffoldState = LocalScaffoldState.current

    val navigationIcon = @Composable {
        if (scaffoldState.hasBackButton) {
            Icon(
                modifier = Modifier.padding(start = 12.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_app_bar_back),
                contentDescription = null,
                tint = AppColors.grey3
            )
        } else {
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
    }

    AppBarScreen(
        title = { Text(text = "sweety home", style = AppTypography.SH3_16, color = AppColors.deepPurple1) },
        navigationIcon = navigationIcon,
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
            )
        },
        hasShadow = scaffoldState.hasShadow
    ) {
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            builder = getMainGraph(navController = navController),
        )
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
) {
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Album,
        BottomNavItem.AddPost,
        BottomNavItem.Calendar,
        BottomNavItem.MyPage
    )

    val scaffoldState = LocalScaffoldState.current

    BottomNavigation(
        modifier = Modifier
            .bottomNavShadow()
            .heightIn(min = 75.dp)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        backgroundColor = Color.White
    ) {
        bottomNavItems.forEach { item ->
            val selected = scaffoldState.selectedBottomNav == item.route
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
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
