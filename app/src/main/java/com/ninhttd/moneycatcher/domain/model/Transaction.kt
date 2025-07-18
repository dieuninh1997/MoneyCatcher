package com.ninhttd.moneycatcher.domain.model

import com.ninhttd.moneycatcher.common.TransactionType
import java.time.LocalDate
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val walletId: String,
    val categoryId: String,
    val transactionType: TransactionType,
    val amount: Long,
    val note: String,
    val transactionDate: LocalDate
)

data class TransactionUiModel(
    val id: String,
    val userId: String,
    val wallet: Wallet,
    val category: Category,
    val transactionType: TransactionType,
    val amount: Long,
    val note: String,
    val transactionDate: LocalDate,
)