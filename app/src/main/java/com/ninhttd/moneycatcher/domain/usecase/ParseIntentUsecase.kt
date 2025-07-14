package com.ninhttd.moneycatcher.domain.usecase

import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.domain.repository.IntentRepository
import javax.inject.Inject

class ParseIntentUsecase @Inject constructor(
    private val repository: IntentRepository
) {
    suspend operator fun invoke(text: String, categoryList: List<String>): Result<List<ParseIntentResponse>> {
        return repository.parseIntent(text, categoryList)
    }
}