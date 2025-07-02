package com.ninhttd.moneycatcher.ui.screen.add

import com.ninhttd.moneycatcher.ui.screen.add.component.AddNewTab
import kotlinx.collections.immutable.persistentListOf

data class AddNewUiState(
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessageIds: List<Int> = persistentListOf(),
    val addNewTab: AddNewTab = AddNewTab.Income,
)