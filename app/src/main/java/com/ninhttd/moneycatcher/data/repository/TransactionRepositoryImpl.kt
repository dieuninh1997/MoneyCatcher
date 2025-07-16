package com.ninhttd.moneycatcher.data.repository

import com.google.gson.Gson
import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import com.ninhttd.moneycatcher.data.model.TransactionDto
import com.ninhttd.moneycatcher.data.remote.IntentApi
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.di.SessionManager
import com.ninhttd.moneycatcher.domain.repository.TransactionRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val intentApi: IntentApi,
    private val auth: Auth,
    private val appPrefs: AppPreferencesManager
) : TransactionRepository {
    override suspend fun getTransactions(): List<TransactionDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("transactions")
                .select()
                .decodeList<TransactionDto>()
            result
        }
    }

    override suspend fun parseIntent(
        text: String,
        categoryList: List<String>
    ): Result<List<ParseIntentResponse>> {
        return try {
            val response = intentApi.parseIntent(text, categoryList)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun ocrInvoice(
        imageBase64: String,
        categoryList: List<String>
    ): Result<ParseIntentResponse> {
        return try {
            val response = intentApi.ocrInvoice(imageBase64, categoryList)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun ocrInvoicePart(
        file: MultipartBody.Part,
        categoryList: List<MultipartBody.Part>
    ): Result<ParseIntentResponse> {
        return try {
            val response = intentApi.ocrInvoice2(file, categoryList)
            Result.success(response)
        } catch (e: Exception) {
            if (e is retrofit2.HttpException && e.code() == 401) {
                val refreshToken = SessionManager.getRefreshToken()
                if (refreshToken.isEmpty()) {
                    Result.failure<Exception>(e)
                } else {
                    try {
                        val refreshResult = auth.refreshSession(refreshToken)
                        val newAccessToken = refreshResult.accessToken
                        val newRefreshToken = refreshResult.refreshToken

                        val currentUser = SessionManager.currentUser
                        currentUser?.let {
                            val updatedUser = it.copy(
                                token = newAccessToken,
                                refreshToken = newRefreshToken
                            )
                            SessionManager.login(updatedUser, appPrefs)
                        }

                        val retryReponse = intentApi.ocrInvoice2(file, categoryList)
                        Result.success(retryReponse)

                    } catch (e2: Exception) {
                        Result.failure<Exception>(e2)
                    }
                }
            }
            Result.failure(e)
        }
    }
}

fun createCategoryPart(categoryList: List<String>): RequestBody {
    val json = Gson().toJson(categoryList) // ví dụ: ["food", "travel"]
    return json.toRequestBody("application/json".toMediaTypeOrNull())
}
