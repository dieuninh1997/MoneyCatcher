package com.ninhttd.moneycatcher

import com.ninhttd.moneycatcher.navigation.NavigationBarScreen
import com.ninhttd.moneycatcher.navigation.Screen

data class MainActivityUiState(
    val startScreen: NavigationBarScreen = NavigationBarScreen.Add,
    var isLoading: Boolean = false
)