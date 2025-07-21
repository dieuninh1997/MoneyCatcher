package com.ninhttd.moneycatcher.di

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ninhttd.moneycatcher.domain.model.UserInfo
import com.ninhttd.moneycatcher.domain.model.Wallet
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import timber.log.Timber
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
        val USER_INFO_KEY = stringPreferencesKey("user_info")
        val CURRENT_WALLET_ID = stringPreferencesKey("current_wallet_id")
    }

    val userFlow: Flow<UserInfo?> = dataStore.data.map { prefs ->
        prefs[USER_INFO_KEY]?.let { json ->
            Json.decodeFromString<UserInfo>(json)
        }
    }

    suspend fun setUser(user: UserInfo) {
        val json = Json.encodeToString(user)
        Timber.tag("AppPrefs").d("Saved user: $json")
        dataStore.edit { prefs ->
            prefs[USER_INFO_KEY] = json
        }
    }

    suspend fun clearUser() {
        dataStore.edit { prefs ->
            prefs.remove(USER_INFO_KEY)
        }
    }

    suspend fun getUser(): UserInfo? {
        val prefs = dataStore.data.first()
        val json = prefs[USER_INFO_KEY] ?: return null
        Timber.tag("AppPrefs").d("Loaded user: $json")
        return try {
            Json.decodeFromString<UserInfo>(json)
        }catch (e: Exception) {
            Timber.tag("AppPrefs").d("Error decoding user: ${e.message}")
            null
        }
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

    suspend fun setCurrentWalletId(id: String) {
        dataStore.edit { prefs ->
            prefs[CURRENT_WALLET_ID] = id
        }
    }

    suspend fun getCurrentWalletId(): String? {
        return dataStore.data.map { it[CURRENT_WALLET_ID] }.firstOrNull()
    }
}