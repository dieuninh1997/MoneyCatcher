package com.ninhttd.moneycatcher.ui.screen.splash

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.di.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appPrefs: AppPreferencesManager
) : ViewModel() {

    var isUserLoggedIn by mutableStateOf<Boolean?>(null)
        private set

    init {
        checkUser()
    }

    private fun checkUser() {
        viewModelScope.launch {
            val user = appPrefs.getUser()
            Timber.tag("SplashVM").d("User loaded = $user")
            if (user != null) {
                SessionManager.currentUser = user
            }
            isUserLoggedIn = user != null
        }
    }
}