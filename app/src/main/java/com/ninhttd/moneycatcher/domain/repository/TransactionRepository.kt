package com.ninhttd.moneycatcher.domain.repository

import com.ninhttd.moneycatcher.data.model.TransactionDto

interface TransactionRepository {
    suspend fun getTransactions(): List<TransactionDto>?
}