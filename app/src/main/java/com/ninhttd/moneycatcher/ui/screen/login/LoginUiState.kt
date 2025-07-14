package com.ninhttd.moneycatcher.ui.screen.login

import com.ninhttd.moneycatcher.domain.model.UserInfo


sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: Result<UserInfo?>) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}