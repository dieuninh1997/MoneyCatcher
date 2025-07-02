package com.ninhttd.moneycatcher.ui.screen.settings

import androidx.annotation.StringRes
import com.ninhttd.moneycatcher.R


data class SettingsUiState(
    val currency: Currency = Currency.USD,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)



enum class Currency(val symbol: String, @StringRes val nameId: Int) {
    USD("$", R.string.currency_usd),
    VND("₫", R.string.currency_vnd)
}