package com.ninhttd.moneycatcher.ui.screen.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddNewiewModel @Inject constructor() : ViewModel() {
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