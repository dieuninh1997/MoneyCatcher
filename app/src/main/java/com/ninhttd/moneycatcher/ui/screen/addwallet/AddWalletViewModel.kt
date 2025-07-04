package com.ninhttd.moneycatcher.ui.screen.addwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.domain.model.WalletDto
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddWalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val appPrefs: AppPreferencesManager
) : ViewModel() {

     fun createWallet(wallet: Wallet, onResult: (Boolean) -> Unit) {
         viewModelScope.launch {
             val walletDto = WalletDto(
                 name = wallet.name,
                 balance = wallet.balance,
                 id = wallet.id,
                 user_id = wallet.userId,
                 is_default = wallet.isDefault
             )
            val success = walletRepository.createWallet(walletDto)
             if(success) {
                 val currentWallets = appPrefs.walletListFlow.first()
                 appPrefs.saveWalletList(currentWallets + wallet)
             }
             onResult(success)
         }
    }
}