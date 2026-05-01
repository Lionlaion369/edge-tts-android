package com.lionlaion.lira.core.story

import com.lionlaion.lira.core.network.ApiClient
import com.lionlaion.lira.core.network.LiraApiService
import com.lionlaion.lira.core.network.LiraRequest

class StoryRepository {

    private val api = ApiClient.retrofit.create(LiraApiService::class.java)

    suspend fun generateStory(prompt: String, apiKey: String): String {
        return try {
            val request = LiraStoryGenerator.createStoryRequest(prompt)

            val response = api.sendMessage(
                authorization = "Bearer $apiKey",
                request = request
            )

            response.body()?.choices?.firstOrNull()?.message?.content
                ?: "Desculpe, não consegui criar a história agora."

        } catch (e: Exception) {
            "Ops! Tive um probleminha. Pode tentar de novo?"
        }
    }
}
