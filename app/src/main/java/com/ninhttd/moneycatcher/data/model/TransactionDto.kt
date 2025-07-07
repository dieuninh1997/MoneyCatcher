package com.ninhttd.moneycatcher.data.model

import com.ninhttd.moneycatcher.di.LocalDateSerializer
import com.ninhttd.moneycatcher.domain.model.Transaction
import com.ninhttd.moneycatcher.domain.model.TransactionType
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class TransactionDto(
    val id: String,
    val user_id: String,
    val wallet_id: String,
    val category_id: String,
    val transaction_type_id: Int,
    val amount: String,
    val note: String,

    @Serializable(with = LocalDateSerializer::class)
    val transaction_date: LocalDate
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            transactionType = TransactionType.fromId(transaction_type_id) ?: TransactionType.EXPENSE,
            userId = user_id,
            walletId = wallet_id,
            categoryId = category_id,
            amount = amount,
            note = note,
            transactionDate = transaction_date,
        )
    }
}


