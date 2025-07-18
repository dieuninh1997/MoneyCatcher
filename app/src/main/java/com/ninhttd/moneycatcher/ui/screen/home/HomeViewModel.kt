package com.ninhttd.moneycatcher.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.common.TimeFilter
import com.ninhttd.moneycatcher.common.TransactionType
import com.ninhttd.moneycatcher.data.model.CategoryDto
import com.ninhttd.moneycatcher.data.model.WalletDto
import com.ninhttd.moneycatcher.domain.model.Category
import com.ninhttd.moneycatcher.domain.model.CategorySummary
import com.ninhttd.moneycatcher.domain.model.TransactionUiModel
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import com.ninhttd.moneycatcher.domain.repository.TransactionRepository
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _transactionsUiFlow = MutableStateFlow<List<TransactionUiModel>>(emptyList())
    val transactionsUiFlow: StateFlow<List<TransactionUiModel>> = _transactionsUiFlow

    private val _groupTransactionsUiFlow =
        MutableStateFlow<List<Pair<LocalDate, List<TransactionUiModel>>>>(emptyList())
    val groupTransactionsUiFlow: StateFlow<List<Pair<LocalDate, List<TransactionUiModel>>>> =
        _groupTransactionsUiFlow

    private val _timeFilter = MutableStateFlow(TimeFilter.MONTHLY)
    val timeFilter: StateFlow<TimeFilter> = _timeFilter

    private val _selectedTransactionType =
        MutableStateFlow<TransactionType?>(TransactionType.EXPENSE) // mac dinh chi tieu
    val selectedTransactionType: StateFlow<TransactionType?> = _selectedTransactionType


    fun setTimeFilter(filter: TimeFilter) {
        _timeFilter.value = filter
    }

    val topSpendingCategories: StateFlow<LoadResult<List<CategorySummary>>> = combine(
        transactionsUiFlow,  // All transactions
        timeFilter, selectedTransactionType
    ) { transactions, filter, selectedType ->

        val now = LocalDate.now()

        // Lọc theo thời gian
        val filtered = transactions.filter { tx ->
            val date = tx.transactionDate
            val inTime = when (filter) {
                TimeFilter.WEEKLY -> date.isAfter(now.minusWeeks(1))
                TimeFilter.MONTHLY -> date.month == now.month && date.year == now.year
                TimeFilter.YEARLY -> date.year == now.year
            }
            val matchType = when (selectedType) {
                TransactionType.INCOME -> tx.transactionType.id == 1
                TransactionType.EXPENSE -> tx.transactionType.id == 2
                else -> true // Nếu không chọn, lấy cả hai
            }
            inTime && matchType
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
        }.sortedByDescending { it.amount }.take(3) // top 3 spending categories
    }.asLoadResult().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LoadResult.Loading())

    init {
        Log.d("HomeViewModel", "🔥 CREATED")
        initialiseUiState()
        fetchTransactionsWithCategory()
    }

    private fun initialiseUiState() {
        //TODO
    }

    fun fetchTransactionsWithCategory() {
        if (_transactionsUiFlow.value.isNotEmpty()
            || _groupTransactionsUiFlow.value.isNotEmpty()
        ) {
            return
        }
        viewModelScope.launch {
            val txDtos = transactionRepository.getTransactions() ?: emptyList()
            val catDtos = categoryRepository.getCategories() ?: emptyList()
            val walletDtos = walletRepository.getWallets() ?: emptyList()

            val categories = catDtos.map { it.asDomainModel() }
            val categoryMap = categories.associateBy { it.id }

            val uiList = txDtos.mapNotNull { tx ->
                val category = categoryMap[tx.category_id.trim()]
                category?.let { ct ->
                    val wallets = walletDtos.map { it.asDomainModel() }
                    val walletMap = wallets.associateBy { it.id }
                    val wallet = walletMap[tx.wallet_id.trim()]
                    wallet?.let { w ->
                        TransactionUiModel(
                            id = tx.id,
                            userId = tx.user_id,
                            wallet = w,
                            category = ct,
                            transactionType = TransactionType.fromId(tx.transaction_type_id)
                                ?: TransactionType.EXPENSE,
                            amount = tx.amount,
                            note = tx.note,
                            transactionDate = tx.transaction_date,
                        )
                    }
                }
            }

            val grouped: List<Pair<LocalDate, List<TransactionUiModel>>> =
                uiList.sortedByDescending { it.transactionDate } // mới nhất trước
                    .groupBy { it.transactionDate }            // gộp theo LocalDate
                    .toSortedMap(compareByDescending { it })   // sort theo ngày giảm dần
                    .toList()

            _transactionsUiFlow.value = uiList
            _groupTransactionsUiFlow.value = grouped
        }
    }

    private fun CategoryDto.asDomainModel(): Category {
        return Category(
            id = this.id, name = this.name, icon = this.icon, isDefalt = this.isDefalt
        )
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

}




