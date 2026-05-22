package com.lionlaion369.voicekit.profiles

import com.lionlaion369.voicekit.core.VoiceGender
import com.lionlaion369.voicekit.core.VoiceProfile

/**
 * Perfis de voz de todas as IAs do ecossistema.
 *
 * PARA ADICIONAR NOVA IA:
 * 1. Copie qualquer bloco abaixo
 * 2. Troque iaId, displayName, edgeVoiceName, rate, pitch
 * 3. Adicione no mapa ALL_VOICES
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
        nativePitch = 1.15f,   // valor já calibrado no projeto Lira
        nativeSpeed = 0.88f,   // valor já calibrado no projeto Lira
        description = "Contadora de histórias cristã para crianças 5-10 anos."
    )

    // Lira no modo narração imersiva (histórias de 7min33s)
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

    // ── WILLA — Assistente pessoal do William ───────────────────
    val WILLA = VoiceProfile(
        iaId = "willa",
        displayName = "Willa",
        gender = VoiceGender.FEMININE,
        edgeVoiceName = "pt-BR-FranciscaNeural",
        rate = "+5%",
        pitch = "+2Hz",
        volume = "+10%",
        nativePitch = 1.05f,
        nativeSpeed = 1.05f,
        description = "Assistente pessoal. Voz madura, calorosa e confiante."
    )

    // ── AEGIS — IA de segurança ─────────────────────────────────
    val AEGIS = VoiceProfile(
        iaId = "aegis",
        displayName = "Aegis",
        gender = VoiceGender.MASCULINE,
        edgeVoiceName = "pt-BR-AntonioNeural",
        rate = "+0%",
        pitch = "-5Hz",
        volume = "+5%",
        nativePitch = 0.95f,
        nativeSpeed = 1.00f,
        description = "IA de segurança. Voz masculina firme e clara."
    )

    // ── THESAURUS — IA de conhecimento ─────────────────────────
    val THESAURUS = VoiceProfile(
        iaId = "thesaurus",
        displayName = "Thesaurus",
        gender = VoiceGender.MASCULINE,
        edgeVoiceName = "pt-BR-DonatoNeural",
        rate = "-5%",
        pitch = "-3Hz",
        volume = "+0%",
        nativePitch = 0.98f,
        nativeSpeed = 0.95f,
        description = "IA de conhecimento. Voz professoral e sábia."
    )

    // ── AURORA — IA [definir descrição] ────────────────────────
    val AURORA = VoiceProfile(
        iaId = "aurora",
        displayName = "Aurora",
        gender = VoiceGender.FEMININE,
        edgeVoiceName = "pt-BR-BrendaNeural",
        rate = "+3%",
        pitch = "+5Hz",
        volume = "+8%",
        nativePitch = 1.10f,
        nativeSpeed = 1.02f,
        description = "Aurora. Voz jovem, clara e energética."
    )

    // ── Mapa de todas as IAs ────────────────────────────────────
    // SEMPRE adicione novas IAs aqui também
    val ALL_VOICES: Map<String, VoiceProfile> = mapOf(
        LIRA.iaId          to LIRA,
        LIRA_STORY_MODE.iaId to LIRA_STORY_MODE,
        WILLA.iaId         to WILLA,
        AEGIS.iaId         to AEGIS,
        THESAURUS.iaId     to THESAURUS,
        AURORA.iaId        to AURORA,
        // NOVA IA: adicione aqui ↓
    )

    /** Busca perfil por ID. Retorna LIRA se não encontrar. */
    fun getById(id: String): VoiceProfile = ALL_VOICES[id] ?: LIRA
}
