package com.ninhttd.moneycatcher.domain.repository

import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.data.model.TransactionDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface TransactionRepository {
    suspend fun getTransactions(): List<TransactionDto>?
    suspend fun parseIntent(text: String, categoryList: List<String>): Result<List<ParseIntentResponse>>
    suspend fun ocrInvoice(imageBase64: String, categoryList: List<String>): Result<ParseIntentResponse>
    suspend fun ocrInvoicePart(file: MultipartBody.Part, categoryList: List<MultipartBody.Part>): Result<ParseIntentResponse>

}