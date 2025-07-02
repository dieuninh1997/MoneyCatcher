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

private val darkColorScheme = darkColorScheme(
    primary = dark_primary,
    primaryContainer = dark_primaryContainer,
    onPrimaryContainer = dark_onPrimaryContainer,
    background = dark_background,
    onBackground = dark_onBackground,
    surface = dark_surface,
    onSurface = dark_onSurface,
    onSurfaceVariant = dark_onSurfaceVariant
)

@Composable
fun AppTheme(
    navController: NavController = rememberNavController(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    content: @Composable () -> Unit
) {
    LaunchedEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = darkColorScheme.background,
            darkIcons = false
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(navBackStackEntry) {
        val currentRoute = navBackStackEntry?.destination?.route
        val inNavigationBarScreen = currentRoute == Screen.NavigationBar.route

        val gestureBarColor = if (inNavigationBarScreen) {
            darkColorScheme.primaryContainer
        } else {
            darkColorScheme.background
        }

        systemUiController.setNavigationBarColor(
            color = gestureBarColor,
            darkIcons = false
        )
    }

    MaterialTheme(
        colorScheme = darkColorScheme,
        shapes = AppShapes,
        typography = AppTypography,
        content = content
    )
}