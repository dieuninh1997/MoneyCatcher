package com.ninhttd.moneycatcher.ui.screen.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.data.mapper.asWrappedList
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.data.model.WalletDto
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val appPrefs: AppPreferencesManager,
    private val walletRepository: WalletRepository,
) : ViewModel() {

    init {
        getWalletList()
    }

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

    fun setCurrentWalletId(id: String) {
        _currentWalletId.value = id
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


    fun createWallet(wallet: Wallet, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val walletDto = WalletDto(
                name = wallet.name,
                balance = wallet.balance,
                id = wallet.id,
                user_id = wallet.userId,
                is_default = wallet.isDefault,
                init_balance = wallet.initBalance
            )
            val success = walletRepository.createWallet(walletDto)
            if (success) {
                val currentWallets = appPrefs.walletListFlow.first()
                appPrefs.saveWalletList(currentWallets + wallet)
            }
            onResult(success)
        }
    }

    fun setWalletAsDefault(walletId: String) {
        viewModelScope.launch {
            walletRepository.setWalletAsDefault(
                walletId
            )
            getWalletList()
        }
    }

    fun deleteWallet(walletId: String) {
        viewModelScope.launch {
            walletRepository.deleteWallet(walletId)
            getWalletList()
        }
    }
}