package io.familymoments.app.feature.bottomnav.screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import io.familymoments.app.R
import io.familymoments.app.core.component.AppBarScreen
import io.familymoments.app.core.graph.getMainGraph
import io.familymoments.app.core.network.AuthErrorManager
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.core.theme.AppTypography.LB2_11
import io.familymoments.app.core.util.LocalScaffoldState
import io.familymoments.app.core.util.keyboardAsState
import io.familymoments.app.feature.bottomnav.BottomNavItem
import io.familymoments.app.feature.bottomnav.component.bottomNavShadow
import io.familymoments.app.feature.bottomnav.viewmodel.MainViewModel
import io.familymoments.app.feature.choosingfamily.activity.ChoosingFamilyActivity
import io.familymoments.app.feature.home.screen.HomeScreenPreview
import io.familymoments.app.feature.login.activity.LoginActivity

@Composable
fun MainScreen(viewModel: MainViewModel, authErrorManager: AuthErrorManager) {
    val navController = rememberNavController()
    val scaffoldState = LocalScaffoldState.current
    val isKeyboardOpen by keyboardAsState()
    val context = LocalContext.current
    val familyUiState = viewModel.familyUiState.collectAsStateWithLifecycle().value

    val appBarUiState = viewModel.appBarUiState.collectAsStateWithLifecycle()

    LaunchedEffect(authErrorManager.needReissueToken) {
        authErrorManager.needReissueToken.collect { event ->
            event.getContentIfNotHandled()?.let {
                viewModel.reissueAccessToken(it)
            }
        }
    }

    LaunchedEffect(authErrorManager.needNavigateToLogin) {
        authErrorManager.needNavigateToLogin.collect { event ->
            event.getContentIfNotHandled()?.let {
                val intent = Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
        }
    }

    LaunchedEffect(familyUiState.familyExist) {
        if (familyUiState.familyExist == false) {
            val intent = Intent(context, ChoosingFamilyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    val navigationIcon = @Composable {
        if (scaffoldState.hasBackButton) {
            Icon(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable { navController.popBackStack() },
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_app_bar_back),
                contentDescription = null,
                tint = AppColors.grey3
            )
        } else {
            AsyncImage(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(34.dp)
                    .clip(shape = CircleShape),
                model = appBarUiState.value.profileImgUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Album,
        BottomNavItem.AddPost,
        BottomNavItem.Calendar,
        BottomNavItem.MyPage
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isBottomNavItem = currentDestination?.hierarchy?.any { currentScreen ->
        bottomNavItems.map { it.route }.contains(currentScreen.route)
    } == true

    val isAddPost = currentDestination?.hierarchy?.any { currentScreen ->
        currentScreen.route == BottomNavItem.AddPost.route
    } == true

    AppBarScreen(
        title = {
            Text(
                text = appBarUiState.value.familyName,
                style = AppTypography.SH3_16,
                color = AppColors.grey8
            )
        },
        navigationIcon = navigationIcon,
        bottomBar = {
            if (isBottomNavItem && !isKeyboardOpen && !isAddPost) {
                BottomNavigationBar(
                    navController = navController,
                    bottomNavItems = bottomNavItems,
                    currentDestination = currentDestination
                )
            }
        },
        hasShadow = scaffoldState.hasShadow
    ) {
        NavHost(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
                .padding(bottom = if (isBottomNavItem && !isKeyboardOpen) 75.dp else 0.dp),
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            builder = getMainGraph(navController = navController),
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    currentDestination: NavDestination?
) {
    NavigationBar(
        modifier = Modifier
            .bottomNavShadow()
            .navigationBarsPadding()
            .height(75.dp)
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        containerColor = Color.White
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
                            tint = if (selected) AppColors.grey8 else AppColors.grey3,
                        )
                        Text(
                            text = stringResource(id = item.labelResId),
                            style = LB2_11,
                            color = if (selected) AppColors.grey8 else AppColors.grey3,
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
    AppBarScreen(
        title = { Text(text = "sweety home", style = AppTypography.SH3_16, color = AppColors.grey8) },
        navigationIcon = {
            Icon(
                modifier = Modifier.padding(start = 12.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_app_bar_back),
                contentDescription = null,
                tint = AppColors.grey3
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = rememberNavController(),
                bottomNavItems = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Album,
                    BottomNavItem.AddPost,
                    BottomNavItem.Calendar,
                    BottomNavItem.MyPage
                ),
                currentDestination = null
            )
        },
    ) {
        HomeScreenPreview()
    }
}
