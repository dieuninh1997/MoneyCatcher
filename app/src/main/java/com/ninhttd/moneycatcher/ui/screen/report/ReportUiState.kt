package com.ninhttd.moneycatcher.ui.screen.add

import kotlinx.collections.immutable.persistentListOf

data class ReportUiState(
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessageIds: List<Int> = persistentListOf()
)