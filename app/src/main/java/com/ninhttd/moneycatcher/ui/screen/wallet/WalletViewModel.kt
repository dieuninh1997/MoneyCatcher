package com.ninhttd.moneycatcher.ui.screen.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    appPrefs: AppPreferencesManager,
) : ViewModel() {

    val walletList = appPrefs.walletListFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
}