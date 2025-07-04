package com.ninhttd.moneycatcher.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Wallet(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val name: String,
    val balance: Long,
    val isDefault: Boolean = false
)