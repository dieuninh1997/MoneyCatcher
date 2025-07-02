package com.ninhttd.moneycatcher.di

import com.ninhttd.moneycatcher.data.AuthRepositoryImpl
import com.ninhttd.moneycatcher.data.remote.AuthApi
import com.ninhttd.moneycatcher.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(api: AuthApi): AuthRepository {
        return AuthRepositoryImpl(api)
    }

}