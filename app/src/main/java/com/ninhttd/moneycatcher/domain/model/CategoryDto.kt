package com.ninhttd.moneycatcher.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String,
    val name: String,
    val icon: String,
    val isDefalt: Boolean = false
)