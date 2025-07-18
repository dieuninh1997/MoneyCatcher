package com.ninhttd.moneycatcher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.navigation.AppNavHost
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.splash.SplashViewModel
import com.ninhttd.moneycatcher.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.net.InetAddress

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isReadyToNavigate by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            !isReadyToNavigate
        }

        testDNS()
        setContent {
            val navController = rememberNavController()
            val splashViewModel: SplashViewModel = hiltViewModel()
            val isUserLoggedIn = splashViewModel.isUserLoggedIn
            val appPrefs = remember { AppPreferencesManager(applicationContext) }

            LaunchedEffect(isUserLoggedIn) {
                if (isUserLoggedIn != null) {
                    navController.navigate(
                        if (isUserLoggedIn) "main_tabs" else Screen.Login.route
                    ) {
                        popUpTo(0) // clear backstack
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
                        navigationBarStartScreen = uiState.startScreen,
                        appPrefs = appPrefs
                    )
                }
            }
        }
    }

    fun testDNS() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val address = InetAddress.getByName("moneycatcher-dev.datasciencedances.com")
                Log.d("DNS", "Resolved to: ${address.hostAddress}")
            } catch (e: Exception) {
                Log.e("DNS", "DNS resolve failed", e)
            }
        }
    }
}
