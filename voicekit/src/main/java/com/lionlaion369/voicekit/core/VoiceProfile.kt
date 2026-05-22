package com.lionlaion369.voicekit.core

/**
 * Modelo de perfil de voz de uma IA.
 * Cada IA do ecossistema (Lira, Willa, Aegis, Thesaurus, Aurora...)
 * tem um VoiceProfile com seus parâmetros calibrados em IaVoices.kt
 */
data class VoiceProfile(
    /** ID único — usado no cache e nos logs. Ex: "lira", "willa" */
    val iaId: String,

    /** Nome para exibição. Ex: "Lira", "Willa" */
    val displayName: String,

    /** Gênero da voz */
    val gender: VoiceGender,

    /**
     * Nome da voz neural Edge TTS.
     * FEMININAS: pt-BR-ThalitaNeural, pt-BR-FranciscaNeural, pt-BR-BrendaNeural
     * MASCULINAS: pt-BR-AntonioNeural, pt-BR-DonatoNeural, pt-BR-FabioNeural
     */
    val edgeVoiceName: String,

    /** Velocidade: "-50%" (lento) a "+50%" (rápido). Padrão: "+0%" */
    val rate: String = "+0%",

    /** Tom: "-20Hz" (grave) a "+20Hz" (agudo). Padrão: "+0Hz" */
    val pitch: String = "+0Hz",

    /** Volume: "-50%" a "+50%". Padrão: "+0%" */
    val volume: String = "+0%",

    /**
     * Pitch para TTS nativo Android (fallback).
     * LIRA já usa 1.15f — mantido aqui para consistência.
     */
    val nativePitch: Float = 1.0f,

    /**
     * Speed para TTS nativo Android (fallback).
     * LIRA já usa 0.88f — mantido aqui para consistência.
     */
    val nativeSpeed: Float = 1.0f,

    /** Descrição do personagem para documentação */
    val description: String = ""
)

enum class VoiceGender { FEMININE, MASCULINE, NEUTRAL }
