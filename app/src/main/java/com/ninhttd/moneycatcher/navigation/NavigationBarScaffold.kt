package com.ninhttd.moneycatcher.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ninhttd.moneycatcher.ui.screen.add.OthersScreen
import com.ninhttd.moneycatcher.ui.screen.add.ReportScreen
import com.ninhttd.moneycatcher.ui.screen.add.component.AddNewScreen
import com.ninhttd.moneycatcher.ui.screen.calendar.CalendarScreen
import com.ninhttd.moneycatcher.ui.screen.home.HomeScreen
import kotlinx.collections.immutable.persistentListOf

@Composable
fun NavigationBarScaffold(
    startScreen: NavigationBarScreen,
    onNavigateDetails: (String) -> Unit,
    onNavigateSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val navigationBarScreens = remember {
        persistentListOf(
            NavigationBarScreen.Home,
            NavigationBarScreen.Calendar,
            NavigationBarScreen.Add,
            NavigationBarScreen.Report,
            NavigationBarScreen.Others,
        )
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showNavigationBar = navigationBarScreens.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = { BottomBarWithFab(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(NavigationBarScreen.Add.route) },
                contentColor = Color.DarkGray,
                backgroundColor = Color.DarkGray,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = if (currentDestination?.route == NavigationBarScreen.Add.route) Color.White else Color.Gray
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) { innerPadding ->
        NavigationBarNavHost(
            navController,
            startScreen = NavigationBarScreen.Add,
            onNavigateDetails = {},
            onNavigateNote = { route ->
                navController.navigate(route)
            },
            onNavigateSettings = {},
            onNavigateEditCategory = {
                navController.navigate(Screen.EditCategory)
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigationBarScaffoldPreview() {
    NavigationBarScaffold(
        NavigationBarScreen.Add,
        onNavigateSettings = {},
        onNavigateDetails = {}
    )
}

@Composable
fun BottomBarWithFab(navController: NavHostController, modifier: Modifier = Modifier) {
    val items = listOf(
        BottomNavItem(
            NavigationBarScreen.Home.route,
            Icons.Default.Home,
            NavigationBarScreen.Home.nameResourceId
        ),
        BottomNavItem(
            NavigationBarScreen.Calendar.route, Icons.Default.CalendarMonth,
            NavigationBarScreen.Calendar.nameResourceId
        ),
        BottomNavItem(NavigationBarScreen.Add.route, null, null),
        BottomNavItem(
            NavigationBarScreen.Report.route,
            Icons.Default.PieChart,
            NavigationBarScreen.Report.nameResourceId
        ),
        BottomNavItem(
            NavigationBarScreen.Others.route,
            Icons.Default.Menu,
            NavigationBarScreen.Others.nameResourceId
        ),
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar(
        tonalElevation = 6.dp,
    ) {
        items.forEachIndexed { index, item ->
            if (index == 2) {
                Spacer(Modifier.weight(1f))
            } else {
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = { navController.navigate(item.route) },
                    icon = {
                        item.iconRes?.let {
                            Icon(
                                it,
                                contentDescription = null,
                                tint = if (currentRoute == item.route) Color.White else Color.Gray
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.label?.let { stringResource(item.label) } ?: "",
                            fontSize = 11.sp,
                            color = if (currentRoute == item.route) Color.White else Color.Gray
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }
}


@Composable
private fun NavigationBarNavHost(
    navController: NavHostController,
    startScreen: NavigationBarScreen,
    onNavigateDetails: (String) -> Unit,
    onNavigateNote: (String) -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateEditCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier
    ) {
        composable(route = NavigationBarScreen.Home.route) {
            HomeScreen(
                onNavigateDetails = onNavigateDetails,
                onNavigateSettings = onNavigateSettings
            )
        }
        composable(route = NavigationBarScreen.Calendar.route) {
            CalendarScreen(
                onNavigateDetails = onNavigateDetails,
                onNavigateSettings = onNavigateSettings
            )
        }
        composable(route = NavigationBarScreen.Add.route) {
            AddNewScreen(
                onNavigateNote = onNavigateNote,
                onNavigateEditCategory = onNavigateEditCategory
            )
        }
        composable(route = NavigationBarScreen.Report.route) {
            ReportScreen(
                onNavigateDetails = onNavigateDetails,
                onNavigateSettings = onNavigateSettings
            )
        }
        composable(route = NavigationBarScreen.Others.route) {
            OthersScreen(
                onNavigateDetails = onNavigateDetails,
                onNavigateSettings = onNavigateSettings
            )
        }
    }
}

