// VoiceManager.kt — Cole isso em qualquer projeto
class VoiceManager(
    private val context: Context,
    private val profile: VoiceProfile
) {
    private val cacheDir = File(context.cacheDir, "voice_cache/${profile.aiName}")
    
    init { cacheDir.mkdirs() }

    suspend fun falar(texto: String) {
        val cacheKey = texto.hashCode().toString()
        val cacheFile = File(cacheDir, "$cacheKey.mp3")
        
        if (cacheFile.exists()) {
            // Usa cache — zero rede, zero custo
            reproduzirArquivo(cacheFile)
            return
        }
        
        try {
            // Tenta Edge TTS (qualidade neural)
            val audio = gerarComEdgeTts(texto)
            cacheFile.writeBytes(audio)
            reproduzirArquivo(cacheFile)
        } catch (e: Exception) {
            // Fallback: TTS nativo do Android
            usarTtsNativo(texto)
        }
    }
    
    private suspend fun gerarComEdgeTts(texto: String): ByteArray {
        // Chame aqui o engine do yynag ou via Cloudflare Worker
        // Ver seção abaixo para implementação via Worker
        TODO("Implementar com engine do yynag")
    }
    
    private fun reproduzirArquivo(arquivo: File) {
        val player = MediaPlayer().apply {
            setDataSource(arquivo.absolutePath)
            prepare()
            start()
        }
    }
    
    private fun usarTtsNativo(texto: String) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Configurar para PT-BR
            }
        }
        tts.language = Locale("pt", "BR")
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
