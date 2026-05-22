package com.lionlaion369.voicekit

import android.content.Context
import com.lionlaion369.voicekit.core.EdgeTtsEngine
import com.lionlaion369.voicekit.core.VoiceProfile
import com.lionlaion369.voicekit.profiles.IaVoices

/**
 * Ponto de entrada único do VoiceKit.
 * Use este objeto em qualquer projeto de IA.
 *
 * INTEGRAÇÃO RÁPIDA:
 *   1. VoiceKit.init(context)          — no onCreate/Application
 *   2. VoiceKit.speak(IaVoices.LIRA, texto) — onde a IA fala
 *   3. VoiceKit.shutdown()             — no onDestroy
 */
object VoiceKit {

    private var engine: EdgeTtsEngine? = null
    private var initialized = false

    /** Inicialize UMA VEZ no onCreate da Application ou Activity. */
    fun init(context: Context) {
        if (!initialized) {
            engine = EdgeTtsEngine(context.applicationContext)
            initialized = true
        }
    }

    /**
     * Faz a IA falar. Chame dentro de uma coroutine.
     *
     * Lira:      VoiceKit.speak(IaVoices.LIRA, texto)
     * Lira hist: VoiceKit.speak(IaVoices.LIRA_STORY_MODE, historia)
     * Willa:     VoiceKit.speak(IaVoices.WILLA, texto)
     * Aegis:     VoiceKit.speak(IaVoices.AEGIS, texto)
     * Thesaurus: VoiceKit.speak(IaVoices.THESAURUS, texto)
     * Aurora:    VoiceKit.speak(IaVoices.AURORA, texto)
     */
    suspend fun speak(profile: VoiceProfile, text: String) {
        check(initialized) { "VoiceKit.init(context) não foi chamado!" }
        engine!!.speak(text, profile)
    }

    /** Para a fala atual. */
    fun stop() = engine?.stop()

    /** Libera recursos. Chame no onDestroy. */
    fun shutdown() {
        engine?.shutdown()
        engine = null
        initialized = false
    }

    /** Busca perfil por ID salvo em preferências. */
    fun profileById(id: String): VoiceProfile = IaVoices.getById(id)
}
