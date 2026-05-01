package com.lionlaion.lira.core.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceStoryTeller(context: Context) {

    private var tts: TextToSpeech? = null
    private var isSpeaking = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("pt", "BR")
                tts?.setSpeechRate(0.95f)
            }
        }
    }

    fun speak(text: String, onComplete: () -> Unit = {}) {
        if (text.isBlank() || tts == null) return

        isSpeaking = true
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "story_utterance")

        tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                isSpeaking = false
                onComplete()
            }
            override fun onError(utteranceId: String?) { isSpeaking = false }
            override fun onStart(utteranceId: String?)        })
    }

    fun stop() {
        tts?.stop()
        isSpeaking = false
    }

    fun shutdown() {
        tts?.shutdown()
    }

    fun isSpeaking(): Boolean = isSpeaking
}
