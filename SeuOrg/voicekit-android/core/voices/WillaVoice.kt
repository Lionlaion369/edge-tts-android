// WillaVoice.kt
object WillaVoice {
    val profile = VoiceProfile(
        voiceName = "pt-BR-FranciscaNeural",  // Feminina, natural
        rate = "+5%",       // um pouco mais rápida — inteligente, ágil
        pitch = "+2Hz",     // tom levemente mais alto — carismática
        volume = "+10%",    // presença vocal forte
        aiName = "Willa"
    )
}

// LiraVoice.kt  
object LiraVoice {
    val profile = VoiceProfile(
        voiceName = "pt-BR-FranciscaNeural",  // mesma base, configuração diferente
        rate = "-10%",      // mais lenta — contadora de histórias
        pitch = "+5Hz",     // mais suave/infantil
        volume = "+5%",
        aiName = "Lira"
    )
    // Para histórias infantis: use rate mais lento e pitch mais alto
    val storymodeProfile = VoiceProfile(
        voiceName = "pt-BR-FranciscaNeural",
        rate = "-20%",      // bem pausado para crianças
        pitch = "+8Hz",     // mais animado
        volume = "+15%",
        aiName = "Lira-Story"
    )
}
