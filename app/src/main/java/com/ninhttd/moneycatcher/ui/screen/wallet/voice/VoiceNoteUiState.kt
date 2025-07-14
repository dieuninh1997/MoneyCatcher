package com.ninhttd.moneycatcher.ui.screen.wallet.voice

import com.ninhttd.moneycatcher.data.model.ParseIntentResponse

sealed class VoiceNoteUiState {
    object Idle : VoiceNoteUiState()
    object Loading : VoiceNoteUiState()
    data class Success(val result: List<ParseIntentResponse>) : VoiceNoteUiState()
    data class Error(val message: String) : VoiceNoteUiState()
}