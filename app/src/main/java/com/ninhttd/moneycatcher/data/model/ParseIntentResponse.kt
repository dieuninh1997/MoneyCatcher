package com.ninhttd.moneycatcher.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ParseIntentResponse (
    val amount: Long,
    val note: String,
    val transaction_type_id: Int,
    val transaction_date: String,
    val category: String,
    val category_index: Int,
    val isAdded: Boolean = false
)
