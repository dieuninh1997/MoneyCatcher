package com.ninhttd.moneycatcher.domain.model

import java.time.LocalDate
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val walletId: String,
    val categoryId: String,
    val transactionType: TransactionType,
    val amount: String,
    val note: String,
    val transactionDate: LocalDate
)

enum class TransactionType(val id: Int, val txName: String, val displayName: String) {
    INCOME(1, "income", "Thu nhập"),
    EXPENSE(2, "expense", "Chi tiêu");

    companion object {
        fun fromId(id: Int): TransactionType? =
            TransactionType.entries.find { it.id == id }
    }
}