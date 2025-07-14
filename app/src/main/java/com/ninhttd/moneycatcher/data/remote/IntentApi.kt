package com.ninhttd.moneycatcher.data.remote

import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IntentApi {
    @FormUrlEncoded
    @POST("api/v1/pasrseintent")
    suspend fun parseIntent(
        @Field("text") text: String,
        @Field("category_list") categoryList: List<String>
    ): List<ParseIntentResponse>
}