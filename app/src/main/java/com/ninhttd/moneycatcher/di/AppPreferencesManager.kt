package com.ninhttd.moneycatcher.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ninhttd.moneycatcher.domain.model.Wallet
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

@Singleton
class AppPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.appDataStore
    companion object {
        val WALLET_LIST_KEY = stringPreferencesKey("wallet_list")
    }

    suspend fun saveWalletList(wallets: List<Wallet>) {
        val jsonString = Json.encodeToString(ListSerializer(Wallet.serializer()), wallets)
        dataStore.edit { prefs ->
            prefs[WALLET_LIST_KEY] = jsonString
        }
    }

    val walletListFlow: Flow<List<Wallet>> = dataStore.data.map { prefs ->
        val jsonString = prefs[WALLET_LIST_KEY] ?: return@map emptyList()
        try {
            Json.decodeFromString(ListSerializer(Wallet.serializer()), jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun clearWalletList() {
        dataStore.edit { prefs ->
            prefs.remove(WALLET_LIST_KEY)
        }
    }

}