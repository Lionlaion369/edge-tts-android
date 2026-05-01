package com.lionlaion.lira

import android.content.Context
import com.lionlaion.lira.core.LiraManager

object Lira {

    private lateinit var manager: LiraManager
    private var isInitialized = false

    fun initialize(context: Context, apiKey: String) {
        manager = LiraManager(context)
        manager.setApiKey(apiKey)
        isInitialized = true
    }

    suspend fun contarHistoria(prompt: String): String {
        if (!isInitialized) return "Lira não foi inicializada."

        return manager.tellStory(prompt)
    }

    fun pararHistoria() {
        if (isInitialized) manager.stopSpeaking()
    }

    fun finalizar() {
        if (isInitialized) manager.shutdown()
    }
}
