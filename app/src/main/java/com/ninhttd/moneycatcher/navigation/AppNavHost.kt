package com.ninhttd.moneycatcher.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.di.SessionManager
import com.ninhttd.moneycatcher.ui.screen.add.OthersScreen
import com.ninhttd.moneycatcher.ui.screen.add.ReportScreen
import com.ninhttd.moneycatcher.ui.screen.add.component.AddNewScreen
import com.ninhttd.moneycatcher.ui.screen.calendar.CalendarScreen
import com.ninhttd.moneycatcher.ui.screen.editcategory.EditCategoryScreen
import com.ninhttd.moneycatcher.ui.screen.home.HomeScreen
import com.ninhttd.moneycatcher.ui.screen.home.HomeViewModel
import com.ninhttd.moneycatcher.ui.screen.login.LoginScreen
import com.ninhttd.moneycatcher.ui.screen.wallet.WalletScreen
import com.ninhttd.moneycatcher.ui.screen.wallet.add.AddWalletScreen
import com.ninhttd.moneycatcher.ui.screen.wallet.detail.WalletDetailScreen
import com.ninhttd.moneycatcher.ui.screen.wallet.ocr.OcrInvoiceScreen
import com.ninhttd.moneycatcher.ui.screen.wallet.voice.VoiceNoteScreen
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorMutedPinkGray

@SuppressLint("ComposeViewModelInjection", "UnrememberedGetBackStackEntry")
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

    val startDestination = if (isLoggedIn) "main_tabs"
    //Screen.NavigationBar.route
    else
        Screen.Login.route

    Scaffold(
        bottomBar = {
            if (startDestination == "main_tabs") {
                BottomBarWithFab(navController)
            }
        },
        floatingActionButton = {
            if (startDestination == "main_tabs") {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavigationBarScreen.Add.route) {
                            popUpTo("main_tabs") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    contentColor = ColorColdPurplePink,
                    backgroundColor = ColorMutedPinkGray,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(
                    onNavigate = {
                        navController.navigate("main_tabs") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            // 👉 Main tabs group
            navigation(
                startDestination = NavigationBarScreen.Home.route,
                route = "main_tabs"
            ) {
                composable(NavigationBarScreen.Home.route) { backStackEntry ->
                    val parentEntry = remember {
                        navController.getBackStackEntry("main_tabs")
                    }
                    val viewModel = hiltViewModel<HomeViewModel>(parentEntry)
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateDetails = { route ->
                            navController.navigate(route)
                        },
                        onNavigateToReport = {
                            navController.navigate(NavigationBarScreen.Report.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToCalendar = {
                            navController.navigate(NavigationBarScreen.Calendar.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                //Calendar tab
                composable(NavigationBarScreen.Calendar.route) {
                    CalendarScreen(onNavigateDetails = {}, onNavigateSettings = {})
                }

                //Add tab
                composable(NavigationBarScreen.Add.route) {
                    AddNewScreen(
                        onNavigateNote = {},
                        onNavigateEditCategory = {},
                        onNavigateDetails = {})
                }

                // report tab
                composable(NavigationBarScreen.Report.route) {
                    ReportScreen(onNavigateDetails = {}, onNavigateSettings = {})
                }

                //others tab
                composable(NavigationBarScreen.Others.route) {
                    OthersScreen(onNavigateDetails = {})
                }

            }

            //Other single-screen routes

            composable(route = Screen.EditCategory.route) {
                EditCategoryScreen(
                    onNavigateUp = { navController.navigateUp() },
                    onNavigateDetails = {}
                )
            }
            composable(route = Screen.Wallet.route) {
                WalletScreen(
                    onNavigateUp = { navController.navigateUp() },
                    onNavigateDetails = {}
                )
            }
            composable(route = Screen.AddWallet.route) {
                AddWalletScreen(onNavigateUp = {
                    navController.navigateUp()
                })
            }
            composable(route = Screen.Search.route) {
                AddWalletScreen(onNavigateUp = {
                    navController.navigateUp()
                })
            }
            composable(route = Screen.VoiceNote.route) {
                VoiceNoteScreen(onNavigateUp = {
                    navController.navigateUp()
                })
            }
            composable(route = Screen.OrcInvoice.route) {
                OcrInvoiceScreen(
                    onNavigateUp = {
                        navController.navigateUp()
                    },
                    onNavigateDetails = {},
                )
            }
            composable(
                route = "${Screen.WalletDetail.route}/{walletId}",
                arguments = listOf(navArgument("walletId") {
                    type =
                        NavType.StringType
                })
            ) { backStackEntry ->
                val walletId = backStackEntry.arguments?.getString("walletId")
                WalletDetailScreen(
                    onNavigateDetails = { route ->

                    },
                    onNavigateUp = {
                        navController.navigateUp()
                    },
                    walletId = walletId ?: ""
                )
            }
        }
    }
}