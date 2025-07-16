package com.ninhttd.moneycatcher.ui.screen.wallet.ocr

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.domain.usecase.OcrInvoiceUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class OcrInvoiceViewModel @Inject constructor(
    private val ocrInvoiceUsecase: OcrInvoiceUsecase
) : ViewModel() {
    var uiState by mutableStateOf<OcrInvoiceUiState>(OcrInvoiceUiState.Idle)

    private val _intentResult = MutableStateFlow<ParseIntentResponse?>(null)
    val intentResult: StateFlow<ParseIntentResponse?> = _intentResult


    fun ocrInvoiceFile(imageBase64: String, categoryList: List<String>) {
        viewModelScope.launch {
            uiState = OcrInvoiceUiState.Loading

            ocrInvoiceUsecase(imageBase64, categoryList)
                .onSuccess { response ->
                    _intentResult.value = response
                    uiState = OcrInvoiceUiState.Success(response)
                }
                .onFailure { error ->
                    uiState = OcrInvoiceUiState.Error(error.message.toString())
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

    fun ocrInvoiceFilePart(file: MultipartBody.Part, categoryList: List<MultipartBody.Part>) {
        viewModelScope.launch {
            uiState = OcrInvoiceUiState.Loading

            ocrInvoiceUsecase(file, categoryList)
                .onSuccess { response ->
                    _intentResult.value = response
                    uiState = OcrInvoiceUiState.Success(response)
                }
                .onFailure { error ->
                    uiState = OcrInvoiceUiState.Error(error.message.toString())
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

    fun resetIntentResult() {
        _intentResult.value = null
        uiState = OcrInvoiceUiState.Idle
    }


    fun markItemAsAdded(index: Int) {
        val oldItem = _intentResult.value ?: return
        val updatedItem = oldItem.copy(isAdded = true)
        _intentResult.value = updatedItem
    }
}