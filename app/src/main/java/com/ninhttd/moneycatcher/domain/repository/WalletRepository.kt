package com.ninhttd.moneycatcher.domain.repository

import com.ninhttd.moneycatcher.data.model.TransactionDto
import com.ninhttd.moneycatcher.data.model.WalletDto


interface WalletRepository {
    suspend fun getWallets(): List<WalletDto>?
    suspend fun createWallet(wallet: WalletDto): Boolean
    suspend fun setWalletAsDefault(walletId: String): Boolean
    suspend fun deleteWallet(walletId: String): Boolean
    suspend fun createTransaction(dto: TransactionDto): Boolean
}