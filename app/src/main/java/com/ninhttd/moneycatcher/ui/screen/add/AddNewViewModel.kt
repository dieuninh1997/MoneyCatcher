package com.ninhttd.moneycatcher.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.CategoryDto
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.domain.model.WalletDto
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewiewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
    private val appPrefs: AppPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddNewUiState())
    val uiState = _uiState.asStateFlow()

    private val _categoriesList = MutableStateFlow<List<Category>?>(listOf())
    val categoriesList: Flow<List<Category>?> = _categoriesList

    val walletList = appPrefs.walletListFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

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

     fun getWalletList() {
        viewModelScope.launch {
            val walletDtos = walletRepository.getWallets()
            walletDtos?.let {
                saveWalletListPrefs(it.map { item -> item.asDomainModel() })
            }
        }
    }

    private fun WalletDto.asDomainModel(): Wallet {
        return Wallet(
            id = this.id,
            name = this.name,
            balance = this.balance,
            isDefault = this.is_default,
            userId = this.user_id
        )
    }

    private fun saveWalletListPrefs(list: List<Wallet>) {
        viewModelScope.launch {
            appPrefs.saveWalletList(list)
        }
    }


    private fun initialiseUiState() {
        //TODO
    }

    fun updateAddNewTab() {
        //TODO
    }
}