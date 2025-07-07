package com.ninhttd.moneycatcher.data.repository

import com.ninhttd.moneycatcher.data.model.CategoryDto
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
) : CategoryRepository {
    override suspend fun getCategories(): List<CategoryDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("categories")
                .select()
                .decodeList<CategoryDto>()
            result
        }
    }

}