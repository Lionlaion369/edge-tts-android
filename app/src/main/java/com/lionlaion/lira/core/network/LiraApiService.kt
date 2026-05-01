

package com.lionlaion.lira.core.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LiraApiService {

    @POST("chat/completions")
    suspend fun sendMessage(
        @Header("Authorization") authorization: String,
        @Body request: LiraRequest
    ): Response<LiraResponse>
}
