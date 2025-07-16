package com.ninhttd.moneycatcher.ui.screen.wallet.ocr

import com.ninhttd.moneycatcher.data.model.ParseIntentResponse

sealed class OcrInvoiceUiState {
    object Idle : OcrInvoiceUiState()
    object Loading : OcrInvoiceUiState()
    data class Success(val result: ParseIntentResponse) : OcrInvoiceUiState()
    data class Error(val message: String) : OcrInvoiceUiState()
}