package com.ninhttd.moneycatcher.data.remote

import com.ninhttd.moneycatcher.di.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SessionManager.getAccessToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}