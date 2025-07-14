package com.ninhttd.moneycatcher.domain.repository

import com.ninhttd.moneycatcher.domain.model.UserInfo

interface AuthRepository {
    suspend fun login(email: String, password: String) : Result<UserInfo?>

}
