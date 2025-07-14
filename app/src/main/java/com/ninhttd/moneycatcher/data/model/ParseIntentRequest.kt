package com.ninhttd.moneycatcher.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParseIntentRequest(
    val text: String,
    @SerialName("category_list") val categoryList: List<String> //["ăn uống", "mua sắm", "Ăn uống"]
)