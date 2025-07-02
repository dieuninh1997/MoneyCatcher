package com.ninhttd.moneycatcher.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val iconRes: ImageVector?,
    @StringRes val label: Int?
)