package com.ninhttd.moneycatcher.ui.screen.wallet

import androidx.lifecycle.ViewModel
import com.ninhttd.moneycatcher.ui.screen.add.AddNewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel

class WalletViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AddNewUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    private fun initialiseUiState() {
        //TODO
    }

    fun updateAddNewTab() {
        //TODO
    }
}

