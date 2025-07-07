package com.ninhttd.moneycatcher.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WalletDto(
    val id: String,
    val user_id: String,
    val name: String,
    val balance: Long,
    val init_balance: Long,
    val is_default: Boolean = false
)