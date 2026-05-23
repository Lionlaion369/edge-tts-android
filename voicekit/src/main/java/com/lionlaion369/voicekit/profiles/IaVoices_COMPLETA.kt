// ══════════════════════════════════════════════════════════════════════
// ARQUIVO: edge-tts-android/voicekit/src/main/java/com/lionlaion369/voicekit/profiles/IaVoices.kt
// SUBSTITUA O CONTEÚDO COMPLETO DESTE ARQUIVO POR ESTE CÓDIGO
// Contém perfis de voz de TODAS as IAs: Lira, Willa, Thaurus
// ══════════════════════════════════════════════════════════════════════

package com.lionlaion369.voicekit.profiles

import com.lionlaion369.voicekit.core.VoiceProfile
import com.lionlaion369.voicekit.core.VoiceGender

/**
 * IaVoices — Perfis de voz de todas as IAs do ecossistema Lionlaion369
 *
 * VOZES FEMININAS PT-BR disponíveis:
 *   pt-BR-ThalitaNeural   → Jovem, calorosa, expressiva  [LIRA]
 *   pt-BR-FranciscaNeural → Madura, sofisticada           [WILLA]
 *   pt-BR-BrendaNeural    → Jovem, vibrante               [futuras]
 *   pt-BR-ElazaNeural     → Suave, delicada               [futuras]
 *   pt-BR-YaraNeural      → Natural, conversacional        [futuras]
 *
 * VOZES MASCULINAS PT-BR disponíveis:
 *   pt-BR-AntonioNeural   → Firme, natural                [futuras]
 *   pt-BR-DonatoNeural    → Maduro, professoral, grave     [THAURUS]
 *   pt-BR-FabioNeural     → Jovem, casual                 [futuras]
 *   pt-BR-NicolauNeural   → Maduro, formal                [futuras]
 */
object IaVoices {

    // ════════════════════════════════════════════════════════════════
    // 🌟 LIRA — Contadora de histórias infantis
    // Voz: FEMININA | ThalitaNeural | jovem, calorosa, expressiva
    // ════════════════════════════════════════════════════════════════

    val LIRA = VoiceProfile(
        iaId = "lira",
        displayName = "Lira",
        gender = VoiceGender.FEMININE,
        edgeVoiceName = "pt-BR-ThalitaNeural",
        rate = "-5%",
        pitch = "+2Hz",
        volume = "+0%",
        nativePitch = 1.1f,
        nativeSpeed = 0.9f,
        description = "Contadora de histórias infantis. Voz jovem, calorosa e expressiva."
    )

    val LIRA_STORY_MODE = VoiceProfile(
        iaId = "lira_story",
        displayName = "Lira — Modo História",
        gender = VoiceGender.FEMININE,
        edgeVoiceName = "pt-BR-ThalitaNeural",
        rate = "-10%",
        pitch = "+3Hz",
        volume = "+5%",
        nativePitch = 1.1f,
        nativeSpeed = 0.85f,
        description = "Modo narração de histórias — mais lento e expressivo."
    )

    // ════════════════════════════════════════════════════════════════
    // 👑 WILLA — Coordenadora Central / IA Principal
    // Voz: FEMININA | FranciscaNeural | madura, sofisticada, autoridade
    // ════════════════════════════════════════════════════════════════

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
        description = "Coordenadora central. Voz madura, sofisticada e com autoridade natural."
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

    // ════════════════════════════════════════════════════════════════
    // 💰 THAURUS — Pilar da Economia / IA Financeira
    // Voz: MASCULINA | DonatoNeural | grave, maduro, barítono profundo
    // ════════════════════════════════════════════════════════════════

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

    // ════════════════════════════════════════════════════════════════
    // 🗺️ MAPA GLOBAL — Todas as vozes indexadas por ID
    // Use este mapa para buscar perfis dinamicamente
    // ════════════════════════════════════════════════════════════════

    val ALL_VOICES: Map<String, VoiceProfile> = mapOf(
        // LIRA
        LIRA.iaId to LIRA,
        LIRA_STORY_MODE.iaId to LIRA_STORY_MODE,
        // WILLA
        WILLA.iaId to WILLA,
        WILLA_COMMAND.iaId to WILLA_COMMAND,
        // THAURUS
        THAURUS.iaId to THAURUS,
        THAURUS_ACTIVATION.iaId to THAURUS_ACTIVATION,
        THAURUS_REPORT.iaId to THAURUS_REPORT,
    )

    /**
     * Busca um perfil de voz pelo ID da IA.
     * Retorna null se não encontrado (use o fallback nativo).
     *
     * Exemplo: IaVoices.getProfile("lira") → retorna LIRA
     */
    fun getProfile(iaId: String): VoiceProfile? = ALL_VOICES[iaId]

    /**
     * Retorna todas as IAs femininas.
     */
    fun getFeminine(): List<VoiceProfile> =
        ALL_VOICES.values.filter { it.gender == VoiceGender.FEMININE }

    /**
     * Retorna todas as IAs masculinas.
     */
    fun getMasculine(): List<VoiceProfile> =
        ALL_VOICES.values.filter { it.gender == VoiceGender.MASCULINE }
}
