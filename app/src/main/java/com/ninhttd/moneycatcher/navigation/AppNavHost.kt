package com.ninhttd.moneycatcher.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.di.SessionManager
import com.ninhttd.moneycatcher.ui.screen.login.LoginScreen

@Composable
fun AppNavHost(
    appPrefs: AppPreferencesManager,
    modifier: Modifier = Modifier,
    navigationBarStartScreen: NavigationBarScreen = NavigationBarScreen.Home,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    var isUserLoaded by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        SessionManager.loadFromPrefs(appPrefs)
        isLoggedIn = SessionManager.currentUser != null
        isUserLoaded = true
    }

    if (!isUserLoaded) {
        // 👇 Show splash or loading UI until user is loaded
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = if (isLoggedIn)
        Screen.NavigationBar.route
    else
        Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(
            route = Screen.Login.route,
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            LoginScreen(onNavigate = {
                navController.navigate(Screen.NavigationBar.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }

        composable(route = Screen.NavigationBar.route) {
            NavigationBarScaffold(
                startScreen = navigationBarStartScreen,
                onNavigateDetails = { coinId: String ->
                },
                onNavigateSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
    }
}