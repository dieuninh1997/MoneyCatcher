package com.ninhttd.moneycatcher.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ninhttd.moneycatcher.data.remote.AuthInterceptor
import com.ninhttd.moneycatcher.data.remote.IntentApi
import com.ninhttd.moneycatcher.data.repository.AuthRepositoryImpl
import com.ninhttd.moneycatcher.data.repository.CategoryRepositoryImpl
import com.ninhttd.moneycatcher.data.repository.TransactionRepositoryImpl
import com.ninhttd.moneycatcher.data.repository.WalletRepositoryImpl
import com.ninhttd.moneycatcher.domain.repository.AuthRepository
import com.ninhttd.moneycatcher.domain.repository.CategoryRepository
import com.ninhttd.moneycatcher.domain.repository.TransactionRepository
import com.ninhttd.moneycatcher.domain.repository.WalletRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

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

    @Provides
    fun provideTransactionRepository(
        postgrest: Postgrest,
        intentApi: IntentApi,
        auth: Auth,
        appPrefs: AppPreferencesManager
    ): TransactionRepository {
        return TransactionRepositoryImpl(postgrest, intentApi, auth, appPrefs)
    }

    @Provides
    fun provideIntentApi(retrofit: Retrofit): IntentApi {
        return retrofit.create(IntentApi::class.java)
    }

    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl("https://moneycatcher-dev.datasciencedances.com/")
            .client(okHttpClient) // dùng client có interceptor
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }


}