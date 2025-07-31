package com.ninhttd.moneycatcher.ui.screen.others

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(OthersUiState())
    val uiState = _uiState.asStateFlow()

    private val _showLogoutConfirmDialog = mutableStateOf(false)
    val showLogoutConfirmDialog = _showLogoutConfirmDialog

    private val _showInfoAppDialog = mutableStateOf(false)
    val showInfoAppDialog = _showInfoAppDialog

    fun onInfoAppClicked() {
        _showInfoAppDialog.value = true
    }

    fun dismissInfoAppDialog() {
        _showInfoAppDialog.value = false
    }

    fun onLogoutClicked() {
        _showLogoutConfirmDialog.value = true
    }

    fun dismissLogoutDialog() {
        _showLogoutConfirmDialog.value = false
    }

    init {
        initialiseUiState()
    }

    private fun initialiseUiState() {
        //TODO
    }

}