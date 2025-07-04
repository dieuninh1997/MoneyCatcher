package com.ninhttd.moneycatcher.data

import com.ninhttd.moneycatcher.domain.model.WalletDto
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
) : WalletRepository {

    override suspend fun getWallets(): List<WalletDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("wallets")
                .select()
                .decodeList<WalletDto>()
            result
        }
    }

    override suspend fun createWallet(wallet: WalletDto): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                postgrest.from("wallets").insert(wallet)
                true
            }
            true
        }catch (e: Exception) {
            throw e
        }
    }

}