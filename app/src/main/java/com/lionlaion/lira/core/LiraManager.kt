package com.lionlaion.lira.core

import android.content.Context
import com.lionlaion.lira.core.story.StoryRepository
import com.lionlaion.lira.core.voice.VoiceStoryTeller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LiraManager(context: Context) {

    private val storyRepository = StoryRepository()
    private val voiceStoryTeller = VoiceStoryTeller(context)

    private var currentApiKey = ""

    fun setApiKey(key: String) {
        currentApiKey = key
    }

    suspend fun tellStory(prompt: String): String = withContext(Dispatchers.IO) {
        if (currentApiKey.isBlank()) {
            return@withContext "Chave da API não configurada."
        }

        val story = storyRepository.generateStory(prompt, currentApiKey)
        voiceStoryTeller.speak(story)
        story
    }

    fun stopSpeaking() {
        voiceStoryTeller.stop()
    }

    fun shutdown() {
        voiceStoryTeller.shutdown()
    }
}
