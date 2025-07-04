package com.ninhttd.moneycatcher.ui.screen.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.di.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    var isUserLoggedIn by mutableStateOf<Boolean?>(null)
        private set

    init {
        checkUser()
    }

    private fun checkUser() {
        viewModelScope.launch {
            val user = SessionManager.currentUser
            isUserLoggedIn = user != null
        }
    }
}