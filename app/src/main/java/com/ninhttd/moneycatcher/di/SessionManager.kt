package com.ninhttd.moneycatcher.di

import com.ninhttd.moneycatcher.domain.model.UserInfo


object SessionManager {
    var currentUser: UserInfo? = null

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
}