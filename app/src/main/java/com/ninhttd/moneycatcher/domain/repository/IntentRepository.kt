package com.ninhttd.moneycatcher.domain.repository

import com.ninhttd.moneycatcher.data.model.ParseIntentResponse

interface IntentRepository {
    suspend fun parseIntent(text: String, categoryList: List<String>): Result<List<ParseIntentResponse>>
}