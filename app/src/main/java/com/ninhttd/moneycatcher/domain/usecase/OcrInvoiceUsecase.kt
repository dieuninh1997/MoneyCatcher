package com.ninhttd.moneycatcher.domain.usecase

import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.domain.repository.TransactionRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class OcrInvoiceUsecase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(imageBase64: String, categoryList: List<String>): Result<ParseIntentResponse> {
        return repository.ocrInvoice(imageBase64, categoryList)
    }

    suspend operator fun invoke(file: MultipartBody.Part, categoryList: List<MultipartBody.Part>): Result<ParseIntentResponse> {
        return repository.ocrInvoicePart(file, categoryList)
    }
}