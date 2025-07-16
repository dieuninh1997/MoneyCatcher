package com.ninhttd.moneycatcher.ui.screen.wallet

sealed class WalletUiState {
    object Idle : WalletUiState()
    object Loading : WalletUiState()
//    data class Success(val user: Result<UserInfo?>) : WalletUiState()
//    data class Error(val message: String) : WalletUiState()
}