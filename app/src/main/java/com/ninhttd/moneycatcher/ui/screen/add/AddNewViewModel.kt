package com.ninhttd.moneycatcher.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.CategoryDto
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewiewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AddNewUiState())
    val uiState = _uiState.asStateFlow()

    private val _categoriesList = MutableStateFlow<List<Category>?>(listOf())
    val categoriesList: Flow<List<Category>?> = _categoriesList

    init {
        initialiseUiState()
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

    private fun initialiseUiState() {
        //TODO
    }

    fun updateAddNewTab() {
        //TODO
    }
}