package com.ninhttd.moneycatcher.domain.model

import java.time.LocalDate

data class SpendingRecord(
    val date: LocalDate,
    val transactions: List<TransactionUiModel>
)