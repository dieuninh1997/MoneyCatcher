package com.ninhttd.moneycatcher.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.ninhttd.moneycatcher.ui.screen.editcategory.EditCategoryScreen
import com.ninhttd.moneycatcher.ui.screen.login.LoginScreen

@Composable
fun AppNavHost(
    navigationBarStartScreen: NavigationBarScreen = NavigationBarScreen.Home,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = {EnterTransition.None},
        exitTransition = { ExitTransition.None}
    ){
        composable(
            route = Screen.Login.route,
            enterTransition ={ fadeIn(animationSpec = tween(500))},
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            LoginScreen(onNavigate = { navController.navigate(Screen.NavigationBar.route) })
        }

        composable(route = Screen.NavigationBar.route) {
            NavigationBarScaffold(
                startScreen = navigationBarStartScreen,
                onNavigateDetails = { coinId: String ->
//                    navController.navigate(Screen.Details.route + "/$coinId")
                },
                onNavigateSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

//        composable(
//            route = Screen.Settings.route,
//            enterTransition = { fadeIn(animationSpec = tween(500)) },
//            exitTransition = { fadeOut(animationSpec = tween(500)) }
//        ) {
//            SettingsScreen(onNavigateUp = { navController.navigateUp() })
//        }
    }
}