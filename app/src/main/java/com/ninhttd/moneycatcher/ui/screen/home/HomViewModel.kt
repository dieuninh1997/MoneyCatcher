package com.ninhttd.moneycatcher.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.common.TimeFilter
import com.ninhttd.moneycatcher.common.TransactionType
import com.ninhttd.moneycatcher.data.model.CategoryDto
import com.ninhttd.moneycatcher.data.model.WalletDto
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.CategorySummary
import com.ninhttd.moneycatcher.domain.model.TransactionUiModel
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import com.ninhttd.moneycatcher.domain.repository.TransactionRepository
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val appPrefs: AppPreferencesManager,
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _transactionsUiFlow = MutableStateFlow<List<TransactionUiModel>>(emptyList())
    val transactionsUiFlow: StateFlow<List<TransactionUiModel>> = _transactionsUiFlow

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

    private val _timeFilter = MutableStateFlow(TimeFilter.MONTHLY)
    val timeFilter: StateFlow<TimeFilter> = _timeFilter

    fun setTimeFilter(filter: TimeFilter) {
        _timeFilter.value = filter
    }

    val topSpendingCategories: StateFlow<List<CategorySummary>> = combine(
        transactionsUiFlow,  // All transactions
        timeFilter
    ) { transactions, filter ->
        val now = LocalDate.now()

        // Lọc theo thời gian
        val filtered = transactions.filter { tx ->
            val date = tx.transactionDate
            when (filter) {
                TimeFilter.WEEKLY -> date.isAfter(now.minusWeeks(1))
                TimeFilter.MONTHLY -> date.month == now.month && date.year == now.year
                TimeFilter.YEARLY -> date.year == now.year
            }
        }

        val total = filtered.sumOf { it.amount }

        // Group theo category
        filtered.groupBy { it.category }.map { (category, txList) ->
            val amount = txList.sumOf { it.amount }
            CategorySummary(
                category = category,
                amount = amount,
                percent = if (total.toDouble() == 0.0) 0f else (amount / total * 100).toFloat()
            )
        }.sortedByDescending { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        initialiseUiState()
        fetchTransactionsWithCategory()
    }

    private fun initialiseUiState() {
        //TODO
    }

    fun fetchTransactionsWithCategory() {
        viewModelScope.launch {
            val txDtos = transactionRepository.getTransactions() ?: emptyList()
            val catDtos = categoryRepository.getCategories() ?: emptyList()

            val categories = catDtos.map { it.asDomainModel() }
            val categoryMap = categories.associateBy { it.id }

            val uiList = txDtos.mapNotNull { tx ->
                val category = categoryMap[tx.category_id.trim()]
                category?.let {
                    TransactionUiModel(
                        id = tx.id,
                        userId = tx.user_id,
                        walletId = tx.wallet_id,
                        category = category,
                        transactionType = TransactionType.fromId(tx.transaction_type_id) ?: TransactionType.EXPENSE,
                        amount = tx.amount,
                        note = tx.note,
                        transactionDate = tx.transaction_date,
                    )
                }
            }

            _transactionsUiFlow.value = uiList
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
            userId = this.user_id,
            initBalance = this.init_balance
        )
    }

    private fun saveWalletListPrefs(list: List<Wallet>) {
        viewModelScope.launch {
            appPrefs.saveWalletList(list)
        }
    }

    fun setCurrentWalletId(id: String) {
        _currentWalletId.value = id
    }

}




