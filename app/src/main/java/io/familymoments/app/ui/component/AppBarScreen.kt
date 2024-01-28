package io.familymoments.app.ui.component

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarScreen(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit) = {},
    bottomBar: @Composable () -> Unit = {},
    hasShadow: Boolean = true,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = if (hasShadow) Modifier.appBarShadow() else Modifier,
                title = title,
                navigationIcon = navigationIcon,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = bottomBar
    ) {
        content()
    }
}
