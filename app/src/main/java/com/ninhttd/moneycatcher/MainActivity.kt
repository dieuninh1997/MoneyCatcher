package com.ninhttd.moneycatcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ninhttd.moneycatcher.navigation.AppNavHost
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.splash.SplashViewModel
import com.ninhttd.moneycatcher.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isReadyToNavigate by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            !isReadyToNavigate
        }

        setContent {
            val navController = rememberNavController()
            val splashViewModel: SplashViewModel = hiltViewModel()
            val isUserLoggedIn = splashViewModel.isUserLoggedIn

            LaunchedEffect(isUserLoggedIn) {
                if (isUserLoggedIn != null) {
                    navController.navigate(if (isUserLoggedIn) Screen.NavigationBar.route else Screen.Login.route) {
                        popUpTo(0)
                    }
                    isReadyToNavigate = true
                }
            }

            AppTheme(navController = navController) {
                val viewModel: MainActivityViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                if (!uiState.isLoading) {
                    AppNavHost(
                        navController = navController,
                        navigationBarStartScreen = uiState.startScreen
                    )
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val uri = intent?.data ?: return
    }
}
