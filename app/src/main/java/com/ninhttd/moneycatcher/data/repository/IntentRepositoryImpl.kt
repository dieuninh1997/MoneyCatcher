package com.ninhttd.moneycatcher.data.repository

import android.util.Log
import com.ninhttd.moneycatcher.data.model.ParseIntentRequest
import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.data.remote.IntentApi
import com.ninhttd.moneycatcher.domain.repository.IntentRepository
import javax.inject.Inject

class IntentRepositoryImpl @Inject constructor(
    private val intentApi: IntentApi
) : IntentRepository {
    override suspend fun parseIntent(
        text: String,
        categoryList: List<String>
    ): Result<List<ParseIntentResponse>> {
        return try {
            val response = intentApi.parseIntent(text, categoryList)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}