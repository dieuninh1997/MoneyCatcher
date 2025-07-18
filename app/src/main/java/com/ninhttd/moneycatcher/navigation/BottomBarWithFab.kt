package com.ninhttd.moneycatcher.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ninhttd.moneycatcher.ui.theme.ColorMutedPinkGray
import com.ninhttd.moneycatcher.ui.theme.ColorSurfaceDark
import com.ninhttd.moneycatcher.ui.theme.ColorZeroWhite

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
        containerColor = ColorSurfaceDark,
        contentColor = ColorMutedPinkGray,
        tonalElevation = 6.dp,
    ) {
        items.forEachIndexed { index, item ->
            if (index == 2) {
                Spacer(Modifier.weight(1f))
            } else {
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo("main_tabs") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        item.iconRes?.let {
                            Icon(
                                it,
                                contentDescription = null,
                                tint = if (currentRoute == item.route) ColorZeroWhite else ColorMutedPinkGray
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.label?.let { stringResource(item.label) } ?: "",
                            fontSize = 11.sp,
                            color = if (currentRoute == item.route) ColorZeroWhite else ColorMutedPinkGray
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }
}

