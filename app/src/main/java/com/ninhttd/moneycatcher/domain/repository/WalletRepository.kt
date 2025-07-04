package com.ninhttd.moneycatcher.domain.repository

import com.ninhttd.moneycatcher.domain.model.WalletDto


interface WalletRepository {
    suspend fun getWallets(): List<WalletDto>?
    suspend fun createWallet(wallet: WalletDto): Boolean
//    suspend fun updateWallet(id:)
}