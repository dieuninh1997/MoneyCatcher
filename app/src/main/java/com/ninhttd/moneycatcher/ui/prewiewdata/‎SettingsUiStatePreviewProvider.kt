package com.ninhttd.moneycatcher.ui.prewiewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ninhttd.moneycatcher.ui.screen.settings.Currency
import com.ninhttd.moneycatcher.ui.screen.settings.SettingsUiState

class SettingsUiStatePreviewProvider : PreviewParameterProvider<SettingsUiState> {
    override val values = sequenceOf(
        SettingsUiState(
            currency = Currency.VND
        ),
        SettingsUiState(
            errorMessage = "No internet connection"
        ),
        SettingsUiState(
            isLoading = true
        )
    )
}