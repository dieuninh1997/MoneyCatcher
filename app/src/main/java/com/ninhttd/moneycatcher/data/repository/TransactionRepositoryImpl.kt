package com.ninhttd.moneycatcher.data.repository

import com.ninhttd.moneycatcher.data.model.TransactionDto
import com.ninhttd.moneycatcher.domain.repository.TransactionRepository
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
) : TransactionRepository {
    override suspend fun getTransactions(): List<TransactionDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("transactions")
                .select()
                .decodeList<TransactionDto>()
            result
        }
    }
}