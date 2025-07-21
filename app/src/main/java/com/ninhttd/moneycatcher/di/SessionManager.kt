package com.ninhttd.moneycatcher.di

import com.ninhttd.moneycatcher.domain.model.UserInfo


object SessionManager {
    var currentUser: UserInfo? = null
    private var _currentWalletId: String? = null

    fun setCurrentWalletId(id: String) {
        _currentWalletId = id
    }

    fun getCurrentWalletId(): String? = _currentWalletId

    suspend fun loadFromPrefs(appPrefs: AppPreferencesManager) {
        currentUser = appPrefs.getUser()
    }

    suspend fun login(user: UserInfo, appPrefs: AppPreferencesManager) {
        currentUser = user
        appPrefs.setUser(user)
    }

    suspend fun logout(appPrefs: AppPreferencesManager) {
        currentUser = null
        appPrefs.clearUser()
    }


    fun getAccessToken(): String {
        return currentUser?.token.orEmpty()
    }

    fun getRefreshToken(): String {
        return currentUser?.refreshToken.orEmpty()
    }
}