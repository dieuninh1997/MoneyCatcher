package com.ninhttd.moneycatcher.ui.screen.others

import kotlinx.collections.immutable.persistentListOf

data class OthersUiState(
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessageIds: List<Int> = persistentListOf()
)