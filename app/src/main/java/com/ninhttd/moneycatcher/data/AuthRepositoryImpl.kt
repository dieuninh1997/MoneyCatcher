package com.ninhttd.moneycatcher.data

import com.ninhttd.moneycatcher.data.remote.AuthApi
import com.ninhttd.moneycatcher.domain.model.User
import com.ninhttd.moneycatcher.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api:AuthApi
): AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            Result.success(User("0", "demo1", "demo1@gmail.com"))
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}