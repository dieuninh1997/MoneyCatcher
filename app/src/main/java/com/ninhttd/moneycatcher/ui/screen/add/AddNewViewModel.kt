package com.ninhttd.moneycatcher.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.common.TransactionType
import com.ninhttd.moneycatcher.data.model.CategoryDto
import com.ninhttd.moneycatcher.data.model.TransactionDto
import com.ninhttd.moneycatcher.data.model.WalletDto
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.di.SessionManager
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.Transaction
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import com.ninhttd.moneycatcher.domain.repository.TransactionRepository
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewiewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository,
    private val appPrefs: AppPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddNewUiState())
    val uiState = _uiState.asStateFlow()

    private val _categoriesList = MutableStateFlow<List<Category>?>(listOf())
    val categoriesList: Flow<List<Category>?> = _categoriesList

    private val _transactionsList = MutableStateFlow<List<Transaction>?>(listOf())
    val transactionsList: Flow<List<Transaction>?> = _transactionsList

    val walletList = appPrefs.walletListFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _currentWalletId = MutableStateFlow<String?>(null)
    val currentWalletId: StateFlow<String?> = _currentWalletId.asStateFlow()

    val currentWallet: Flow<Wallet?> = combine(walletList, currentWalletId) { list, id ->
        list.find { it.id == id }
    }

    private val _currentUser = MutableStateFlow<UserInfo?>(SessionManager.currentUser)
    val currentUser: StateFlow<UserInfo?> = _currentUser

    fun reloadUser() {
        _currentUser.value = SessionManager.currentUser
    }

    fun setCurrentWalletId(id: String) {
        _currentWalletId.value = id
    }


    init {
        initialiseUiState()
        getCategoriesList()
        _currentUser.value = SessionManager.currentUser
    }

    private fun getTransactionsList() {
        viewModelScope.launch {
            val transactions = transactionRepository.getTransactions()
            _transactionsList.emit(transactions?.map { it -> it.asDomainModel() })
        }
    }

    private fun TransactionDto.asDomainModel(): Transaction {
        return Transaction(
            id = this.id,
            userId = this.user_id,
            walletId = this.wallet_id,
            categoryId = this.category_id,
            transactionType = TransactionType.fromId(this.transaction_type_id) ?: TransactionType.EXPENSE,
            amount = this.amount,
            note = this.note,
            transactionDate = this.transaction_date,
        )
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

    fun createTransaction(transaction: Transaction, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val transactionDto = TransactionDto(
                id = transaction.id,
                user_id = transaction.userId,
                wallet_id = transaction.walletId,
                category_id = transaction.categoryId,
                transaction_type_id = transaction.transactionType.id,
                amount = transaction.amount,
                note = transaction.note,
                transaction_date = transaction.transactionDate
            )
            val success = walletRepository.createTransaction(transactionDto)
            getTransactionsList()
            onResult(success)
        }
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
            userId = this.user_id,
            initBalance = this.init_balance
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