package com.ninhttd.moneycatcher.data.remote

import com.ninhttd.moneycatcher.data.model.ParseIntentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IntentApi {
    @FormUrlEncoded
    @POST("api/v1/pasrseintent")
    suspend fun parseIntent(
        @Field("text") text: String,
        @Field("category_list") categoryList: List<String>
    ): List<ParseIntentResponse>


    @FormUrlEncoded
    @POST("api/v1/ocr")
    suspend fun ocrInvoice(
        @Field("file") imageBase64: String,
        @Field("category_list") categoryList: List<String>
    ): ParseIntentResponse

    @Multipart
    @POST("api/v1/ocr")
    suspend fun ocrInvoice2(
        @Part file: MultipartBody.Part,
        @Part categoryList: List<MultipartBody.Part>
    ): ParseIntentResponse
}