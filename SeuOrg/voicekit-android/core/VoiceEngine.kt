// VoiceEngine.kt — Interface universal para qualquer provider de voz
interface VoiceEngine {
    suspend fun speak(text: String, profile: VoiceProfile): Result<Unit>
    suspend fun generateAudio(text: String, profile: VoiceProfile): Result<ByteArray>
    fun stop()
    fun isReady(): Boolean
}

// VoiceProfile.kt — Perfil de voz de cada IA
data class VoiceProfile(
    val voiceName: String,       // ex: "pt-BR-FranciscaNeural"
    val rate: String = "+0%",    // velocidade: "-10%" a "+20%"
    val pitch: String = "+0Hz",  // tom: "-5Hz" a "+10Hz"
    val volume: String = "+0%",  // volume: "+0%" a "+50%"
    val aiName: String = "IA"    // nome para logs/cache
)
