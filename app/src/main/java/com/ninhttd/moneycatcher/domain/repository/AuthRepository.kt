package com.ninhttd.moneycatcher.domain.repository

import io.github.jan.supabase.auth.user.UserInfo

interface AuthRepository {
    suspend fun login(email: String, password: String) : Result<UserInfo?>

}
