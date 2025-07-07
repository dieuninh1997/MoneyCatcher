package com.ninhttd.moneycatcher.domain.repository

import com.ninhttd.moneycatcher.data.model.CategoryDto

interface CategoryRepository {
    suspend fun getCategories(): List<CategoryDto>?
}