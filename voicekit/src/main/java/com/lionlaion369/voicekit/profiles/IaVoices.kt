package com.lionlaion369.voicekit.profiles

import com.lionlaion369.voicekit.core.VoiceGender
import com.lionlaion369.voicekit.core.VoiceProfile

/**
 * Perfis de voz de todas as IAs do ecossistema.
 */
object IaVoices {

    // ── LIRA — Contadora de histórias infantis ──────────────────
    val LIRA = VoiceProfile(
        iaId = "lira",
        displayName = "Lira",
        gender = VoiceGender.FEMININE,
        edgeVoiceName = "pt-BR-ThalitaNeural",
        rate = "-15%",
        pitch = "+8Hz",
        volume = "+10%",
        nativePitch = 1.15f,
        nativeSpeed = 0.88f,
        description = "Contadora de histórias cristã para crianças 5-10 anos."
    )

    val LIRA_STORY_MODE = VoiceProfile(
        iaId = "lira_story",
        displayName = "Lira (Modo História)",
        gender = VoiceGender.FEMININE,
        edgeVoiceName = "pt-BR-ThalitaNeural",
        rate = "-25%",
        pitch = "+12Hz",
        volume = "+15%",
        nativePitch = 1.20f,
        nativeSpeed = 0.80f,
        description = "Lira em modo narração total. Histórias de 7min33s."
    )

    // ── WILLA — Assistente pessoal ─────────────────────────────
    val WILLA = VoiceProfile(
        iaId = "willa",
        displayName = "Willa",
        gender = VoiceGender.FEMININE,
        edgeVoiceName = "pt-BR-FranciscaNeural",
        rate = "+0%",
        pitch = "-2Hz",
        volume = "+0%",
        nativePitch = 0.95f,
        nativeSpeed = 1.0f,
        description = "Coordenadora central Willa. Voz madura, sofisticada."
    )

    val WILLA_COMMAND = VoiceProfile(
        iaId = "willa_command",
        displayName = "Willa — Modo Comando",
        gender = VoiceGender.FEMININE,
        edgeVoiceName = "pt-BR-FranciscaNeural",
        rate = "-5%",
        pitch = "-4Hz",
        volume = "+10%",
        nativePitch = 0.9f,
        nativeSpeed = 0.95f,
        description = "Modo comando — fala com autoridade máxima."
    )

    // ── THAURUS — Pilar da Economia ─────────────────────────────
    val THAURUS = VoiceProfile(
        iaId = "thaurus",
        displayName = "Thaurus",
        gender = VoiceGender.MASCULINE,
        edgeVoiceName = "pt-BR-DonatoNeural",
        rate = "-5%",
        pitch = "-8Hz",
        volume = "+5%",
        nativePitch = 0.85f,
        nativeSpeed = 0.9f,
        description = "Pilar da Economia. Voz masculina grave, firme e madura."
    )

    val THAURUS_ACTIVATION = VoiceProfile(
        iaId = "thaurus_activation",
        displayName = "Thaurus — Ativação",
        gender = VoiceGender.MASCULINE,
        edgeVoiceName = "pt-BR-DonatoNeural",
        rate = "-10%",
        pitch = "-10Hz",
        volume = "+15%",
        nativePitch = 0.8f,
        nativeSpeed = 0.85f,
        description = "Modo ativação — máxima presença e gravidade."
    )

    val THAURUS_REPORT = VoiceProfile(
        iaId = "thaurus_report",
        displayName = "Thaurus — Relatório",
        gender = VoiceGender.MASCULINE,
        edgeVoiceName = "pt-BR-DonatoNeural",
        rate = "+0%",
        pitch = "-6Hz",
        volume = "+0%",
        nativePitch = 0.88f,
        nativeSpeed = 1.0f,
        description = "Modo relatório — dados financeiros claros e precisos."
    )

    // ── Mapa de todas as IAs ────────────────────────────────────
    val ALL_VOICES: Map<String, VoiceProfile> = mapOf(
        LIRA.iaId            to LIRA,
        LIRA_STORY_MODE.iaId to LIRA_STORY_MODE,
        WILLA.iaId           to WILLA,
        WILLA_COMMAND.iaId   to WILLA_COMMAND,
        THAURUS.iaId         to THAURUS,
        THAURUS_ACTIVATION.iaId to THAURUS_ACTIVATION,
        THAURUS_REPORT.iaId  to THAURUS_REPORT,
        // Adicione novas vozes aqui ↓
    )

    /** Busca perfil por ID. Retorna LIRA se não encontrar. */
    fun getById(id: String): VoiceProfile = ALL_VOICES[id] ?: LIRA
}
