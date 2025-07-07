package com.ninhttd.moneycatcher.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String,
    val name: String,
    val icon: String,
    val isDefalt: Boolean = false
)