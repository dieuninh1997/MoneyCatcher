package com.ninhttd.moneycatcher.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val email: String,
    val name: String,
    val token: String,
)