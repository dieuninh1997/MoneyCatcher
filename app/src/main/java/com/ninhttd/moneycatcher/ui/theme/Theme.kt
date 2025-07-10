package com.ninhttd.moneycatcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ninhttd.moneycatcher.navigation.Screen

private val DarkPinkColorScheme = darkColorScheme(
    primary = ColorPinkPrimary,
    primaryContainer = ColorPinkPrimaryContainer,
    onPrimaryContainer = ColorOnPrimaryContainer,
    background = ColorBackgroundDark,
    onBackground = ColorOnBackgroundDark,
    surface = ColorSurfaceDark,
    onSurface = ColorOnSurfaceDark,
    surfaceVariant = ColorOnSurfaceVariantDark
)

@Composable
fun AppTheme(
    navController: NavController = rememberNavController(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    content: @Composable () -> Unit
) {
    LaunchedEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = DarkPinkColorScheme.background,
            darkIcons = false
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(navBackStackEntry) {
        val currentRoute = navBackStackEntry?.destination?.route
        val inNavigationBarScreen = currentRoute == Screen.NavigationBar.route

        val gestureBarColor = if (inNavigationBarScreen) {
            DarkPinkColorScheme.primaryContainer
        } else {
            DarkPinkColorScheme.background
        }

        systemUiController.setNavigationBarColor(
            color = gestureBarColor,
            darkIcons = false
        )
    }

    MaterialTheme(
        colorScheme = DarkPinkColorScheme,
        shapes = AppShapes,
        typography = AppTypography,
        content = content
    )
}