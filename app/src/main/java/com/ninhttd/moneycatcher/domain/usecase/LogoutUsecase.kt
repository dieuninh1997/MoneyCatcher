package com.ninhttd.moneycatcher.domain.usecase

import com.ninhttd.moneycatcher.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUsecase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.logout()
}