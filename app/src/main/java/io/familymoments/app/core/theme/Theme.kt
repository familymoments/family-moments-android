package io.familymoments.app.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val darkColorScheme = darkColorScheme(
    primary = AppColors.purple2,
    secondary = AppColors.pink1
)

private val lightColorScheme = lightColorScheme(
    primary = AppColors.purple2,
    secondary = AppColors.pink1,
    background = Color.White,
    tertiary = Color.White,
    surface = Color.White
)

@Composable
fun FamilyMomentsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    navigationBarColor: Color = Color.White,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, navigationBarColor) {
        systemUiController.setStatusBarColor(
            color = Color.White,
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = true
        )

        onDispose {  }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography.typography,
        content = content
    )
}
