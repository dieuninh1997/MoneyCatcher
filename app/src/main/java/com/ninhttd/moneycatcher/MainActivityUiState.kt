package com.ninhttd.moneycatcher

import com.ninhttd.moneycatcher.navigation.NavigationBarScreen
import com.ninhttd.moneycatcher.navigation.Screen

data class MainActivityUiState(
    val startScreen: NavigationBarScreen = NavigationBarScreen.Home,
    var isLoading: Boolean = false
)