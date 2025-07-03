package com.ninhttd.moneycatcher.navigation

import androidx.annotation.StringRes
import com.ninhttd.moneycatcher.R

sealed class Screen(val route: String) {
    data object Login: Screen("login_screen")
    data object Details: Screen("details_screen")
    data object VoiceNote: Screen("voice_note_screen")
    data object ManualNote: Screen("manual_note_screen")
    data object Settings: Screen("settings_screen")
    data object EditCategory: Screen("edit_category_screen")
    data object NavigationBar: Screen("navigation_bar_screen")
}

sealed class NavigationBarScreen(
    val route: String,
    @StringRes val nameResourceId: Int
) {
    data object Home: NavigationBarScreen("home_screen", R.string.home_screen)
    data object Report: NavigationBarScreen("report_screen", R.string.report_screen)
    data object Add: NavigationBarScreen("add_new_screen", 0)
    data object Calendar: NavigationBarScreen("calendar_screen", R.string.calendar_screen)
    data object Others: NavigationBarScreen("others_screen", R.string.others_screen)
}