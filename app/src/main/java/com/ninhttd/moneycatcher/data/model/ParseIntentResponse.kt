package com.ninhttd.moneycatcher.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ParseIntentResponse (
    val amount: Long,
    val note: String,
    val transaction_type_id: Int? = null,
    val transaction_date: String? = null,
    val category: String,
    val category_index: Int,
    val isAdded: Boolean = false
)

fun ParseIntentResponse.isValid(): Boolean {
    return transaction_type_id != null && !transaction_date.isNullOrBlank()
}