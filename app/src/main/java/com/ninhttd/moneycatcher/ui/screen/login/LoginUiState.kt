package com.ninhttd.moneycatcher.ui.screen.login

import com.ninhttd.moneycatcher.domain.model.User

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}