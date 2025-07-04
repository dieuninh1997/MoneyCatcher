package com.ninhttd.moneycatcher.ui.screen.editcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.CategoryDto
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {
    private val _categoriesList = MutableStateFlow<List<Category>?>(listOf())
    val categoriesList: Flow<List<Category>?> = _categoriesList

    init {
        getCategoriesList()
    }

    private fun getCategoriesList() {
        viewModelScope.launch {
            val categories = categoryRepository.getCategories()
            _categoriesList.emit(categories?.map { it -> it.asDomainModel() })
        }
    }

    private fun CategoryDto.asDomainModel(): Category {
        return Category(
            id = this.id,
            name = this.name,
            icon = this.icon,
            isDefalt = this.isDefalt
        )
    }
}