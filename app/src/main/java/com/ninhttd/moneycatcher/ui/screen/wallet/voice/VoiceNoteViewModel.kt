package com.ninhttd.moneycatcher.ui.screen.wallet.voice

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.domain.usecase.ParseIntentUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VoiceNoteViewModel @Inject constructor(
    private val parseIntentUsecase: ParseIntentUsecase
) : ViewModel() {
    private val _intentResult = MutableStateFlow<List<ParseIntentResponse>?>(null)
    val intentResult: StateFlow<List<ParseIntentResponse>?> = _intentResult

    var uiState by mutableStateOf<VoiceNoteUiState>(VoiceNoteUiState.Idle)

    fun parseSpokenText(text: String, categoryList: List<String>) {
        viewModelScope.launch {
            uiState = VoiceNoteUiState.Loading
            parseIntentUsecase(text, categoryList)
                .onSuccess { response ->
                    _intentResult.value = response
                    uiState = VoiceNoteUiState.Success(response)
                }
                .onFailure { error ->
                    uiState = VoiceNoteUiState.Error(error.message.toString())
                    // log hoặc thông báo lỗi
                    if (error is HttpException) {
                        val errorBody = error.response()?.errorBody()?.string()
                        val code = error.code()
                        Log.e("API_ERROR", "HTTP $code: $errorBody")
                        Timber.tag("NINH").e("HTTP $code - $errorBody")
                    } else {
                        Timber.tag("NINH").e("Unknown error: ${error.message}")
                    }
                }
        }
    }

    fun removeIntentResult(item: ParseIntentResponse) {
        val currentList = _intentResult.value?.toMutableList() ?: return
        currentList.remove(item)
        _intentResult.value = currentList
    }

    fun resetIntentResult() {
        _intentResult.value = null
        uiState = VoiceNoteUiState.Idle
    }


    fun markItemAsAdded(index: Int) {
        val currentList = _intentResult.value ?: return

        if (index !in currentList.indices) return

        val updatedList = currentList.toMutableList()
        val oldItem = updatedList[index]
        val updatedItem = oldItem.copy(isAdded = true)
        updatedList[index] = updatedItem

        _intentResult.value = updatedList
    }
}