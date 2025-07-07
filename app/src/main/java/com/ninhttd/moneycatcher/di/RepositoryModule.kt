package com.ninhttd.moneycatcher.di

import com.ninhttd.moneycatcher.data.repository.AuthRepositoryImpl
import com.ninhttd.moneycatcher.data.repository.CategoryRepositoryImpl
import com.ninhttd.moneycatcher.data.repository.WalletRepositoryImpl
import com.ninhttd.moneycatcher.domain.repository.AuthRepository
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(auth: Auth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    fun provideCategoryRepository(postgrest: Postgrest): CategoryRepository {
        return CategoryRepositoryImpl(postgrest)
    }


    @Provides
    fun provideWalletRepository(postgrest: Postgrest): WalletRepository {
        return WalletRepositoryImpl(postgrest)
    }

}