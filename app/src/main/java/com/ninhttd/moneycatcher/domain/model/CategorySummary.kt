package com.ninhttd.moneycatcher.domain.model

data class CategorySummary(
    val category: Category,
    val amount: Long,
    val percent: Float
)