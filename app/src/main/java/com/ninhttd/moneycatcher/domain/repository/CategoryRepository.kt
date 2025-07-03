package com.ninhttd.moneycatcher.domain.repository

import com.ninhttd.moneycatcher.domain.model.CategoryDto

interface CategoryRepository {
    suspend fun getCategories(): List<CategoryDto>?
}