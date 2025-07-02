package com.ninhttd.moneycatcher.ui.screen.home

import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessageIds: List<Int> = persistentListOf()
)