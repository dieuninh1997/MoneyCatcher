package com.ninhttd.moneycatcher.ui.screen.login

import io.github.jan.supabase.auth.user.UserInfo

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: Result<UserInfo?>) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}