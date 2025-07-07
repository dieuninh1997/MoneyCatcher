package com.ninhttd.moneycatcher.data.repository

import com.ninhttd.moneycatcher.data.model.TransactionDto
import com.ninhttd.moneycatcher.data.model.WalletDto
import com.ninhttd.moneycatcher.di.SessionManager
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
                {
                    filter {
                        eq("user_id", SessionManager.currentUser?.id.toString())
                    }
                }
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
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun setWalletAsDefault(walletId: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                postgrest.from("wallets")
                    .update(
                        mapOf("is_default" to false)
                    ) {
                        filter {
                            eq("user_id", SessionManager.currentUser?.id.toString())
                        }
                    }
                postgrest.from("wallets")
                    .update(mapOf("is_default" to true))
                    {
                        filter {
                            eq("id", walletId)
                        }
                    }
                true
            }
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteWallet(walletId: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                postgrest.from("wallets")
                    .delete {
                        filter {
                            eq("id", walletId)
                        }
                    }
                true
            }
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createTransaction(data: TransactionDto): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                postgrest.from("transactions").insert(data)
                true
            }
            true
        } catch (e: Exception) {
            throw e
        }
    }

}