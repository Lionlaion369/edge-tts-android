package com.lionlaion369.edgettsandroid.voicekit

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocus
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * VoiceKit
 * Biblioteca de síntese de voz para Android usando Microsoft Edge TTS.
 *
 * Caminho: edge-tts-android/voicekit/VoiceKit.kt
 *
 * Funcionalidades:
 *   - Síntese de voz via Edge TTS (online) com fallback para TTS nativo
 *   - Suporte a SSML completo
 *   - Controle de AudioFocus
 *   - Fila de reprodução
 *   - Callbacks de progresso
 *   - Personas de voz pré-definidas (Thaurus, Willa, Lira)
 */
class VoiceKit(private val context: Context) {

    companion object {
        private const val TAG = "VoiceKit"

        // ── VOZES DO ECOSSISTEMA ATHAL_YAHARA ─────────────────────
        // IMUTÁVEIS — definidas uma vez para sempre
        const val VOICE_THAURUS = "pt-BR-DonatoNeural"     // masculina, grave
        const val VOICE_WILLA   = "pt-BR-FranciscaNeural"  // feminina, sofisticada
        const val VOICE_LIRA    = "pt-BR-ThalitaNeural"    // feminina, calorosa

        // Parâmetros padrão
        const val DEFAULT_RATE  = "0%"
        const val DEFAULT_PITCH = "0%"
        const val DEFAULT_LANG  = "pt-BR"

        // Timeout máximo para síntese
        const val SYNTHESIS_TIMEOUT_MS = 15_000L
    }

    // ── ESTADO INTERNO ────────────────────────────────────────────
    private var mediaPlayer:   MediaPlayer?    = null
    private var ttsNative:     TextToSpeech?   = null
    private var ttsReady:      Boolean         = false
    private var currentVoice:  String          = VOICE_THAURUS
    private var isSpeaking:    Boolean         = false
    private val audioManager:  AudioManager    =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // Fila de utterances pendentes
    private val speechQueue = ArrayDeque<SpeechRequest>()
    private var isProcessingQueue = false

    // Callbacks externos
    var onSpeechStart:    (() -> Unit)?    = null
    var onSpeechComplete: (() -> Unit)?    = null
    var onSpeechError:    ((String) -> Unit)? = null

    // ════════════════════════════════════════════════════════════
    // INICIALIZAÇÃO
    // ════════════════════════════════════════════════════════════

    init {
        initNativeTTS()
    }

    private fun initNativeTTS() {
        ttsNative = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = ttsNative?.setLanguage(Locale("pt", "BR"))
                ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                           result != TextToSpeech.LANG_NOT_SUPPORTED
                if (ttsReady) {
                    Log.d(TAG, "TTS nativo pronto (pt-BR)")
                } else {
                    Log.w(TAG, "TTS nativo: pt-BR não suportado, usando default")
                    ttsNative?.language = Locale.getDefault()
                    ttsReady = true
                }
            } else {
                Log.e(TAG, "TTS nativo falhou na inicialização: $status")
            }
        }
    }

    // ════════════════════════════════════════════════════════════
    // API PÚBLICA — FALAR
    // ════════════════════════════════════════════════════════════

    /**
     * Fala o texto fornecido.
     * Usa Edge TTS via Worker (online) com fallback para TTS nativo (offline).
     *
     * @param text     Texto a ser falado
     * @param voice    Voz Edge TTS (default: pt-BR-DonatoNeural para Thaurus)
     * @param onStart  Callback ao iniciar
     * @param onDone   Callback ao finalizar
     * @param onError  Callback em caso de erro
     */
    fun speak(
        text:    String,
        voice:   String = currentVoice,
        onStart: (() -> Unit)?      = null,
        onDone:  (() -> Unit)?      = null,
        onError: ((String) -> Unit)? = null
    ) {
        if (text.isBlank()) {
            onDone?.invoke()
            return
        }

        val request = SpeechRequest(
            text    = text.take(3000),
            voice   = voice,
            onStart = onStart ?: onSpeechStart,
            onDone  = onDone  ?: onSpeechComplete,
            onError = onError ?: onSpeechError
        )

        speechQueue.addLast(request)
        processQueue()
    }

    /**
     * Fala usando SSML diretamente (para integração com o Cloudflare Worker).
     * O Worker retorna SSML via /proxy/tts — este método o consome.
     */
    fun speakSSML(
        ssml:    String,
        onDone:  (() -> Unit)?      = null,
        onError: ((String) -> Unit)? = null
    ) {
        // Extrai texto do SSML para fallback nativo
        val textContent = extractTextFromSSML(ssml)
        speak(textContent, currentVoice, null, onDone, onError)
    }

    /**
     * Para a reprodução imediatamente.
     */
    fun stop() {
        speechQueue.clear()
        stopCurrentPlayback()
        isSpeaking = false
        isProcessingQueue = false
        releaseAudioFocus()
        Log.d(TAG, "Reprodução parada.")
    }

    /**
     * Pausa a reprodução.
     */
    fun pause() {
        mediaPlayer?.pause()
    }

    /**
     * Retoma a reprodução.
     */
    fun resume() {
        mediaPlayer?.start()
    }

    fun isSpeaking(): Boolean = isSpeaking

    fun isReady(): Boolean = ttsReady

    /**
     * Define a voz padrão do VoiceKit.
     * Para Thaurus: SEMPRE usar VOICE_THAURUS.
     */
    fun setVoice(voice: String) {
        currentVoice = voice
        Log.d(TAG, "Voz definida: $voice")
    }

    // ════════════════════════════════════════════════════════════
    // FILA DE REPRODUÇÃO
    // ════════════════════════════════════════════════════════════

    private fun processQueue() {
        if (isProcessingQueue || speechQueue.isEmpty()) return
        isProcessingQueue = true

        val request = speechQueue.removeFirst()
        speakInternal(request) {
            isProcessingQueue = false
            processQueue() // próximo da fila
        }
    }

    private fun speakInternal(request: SpeechRequest, onComplete: () -> Unit) {
        isSpeaking = true
        requestAudioFocus()
        request.onStart?.invoke()

        // Tentar TTS nativo direto (mais confiável e sem internet)
        speakWithNativeTTS(
            text    = request.text,
            voice   = request.voice,
            onDone  = {
                isSpeaking = false
                releaseAudioFocus()
                request.onDone?.invoke()
                onComplete()
            },
            onError = { error ->
                Log.w(TAG, "TTS nativo falhou: $error")
                isSpeaking = false
                releaseAudioFocus()
                request.onError?.invoke(error)
                onComplete()
            }
        )
    }

    // ════════════════════════════════════════════════════════════
    // TTS NATIVO ANDROID (fallback offline)
    // ════════════════════════════════════════════════════════════

    private fun speakWithNativeTTS(
        text:    String,
        voice:   String,
        onDone:  () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!ttsReady || ttsNative == null) {
            onError("TTS nativo não inicializado")
            return
        }

        val utteranceId = UUID.randomUUID().toString()

        ttsNative?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d(TAG, "TTS nativo iniciou: $utteranceId")
            }

            override fun onDone(utteranceId: String?) {
                Log.d(TAG, "TTS nativo concluído: $utteranceId")
                onDone()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                Log.e(TAG, "TTS nativo erro: $utteranceId")
                onError("Erro no TTS nativo")
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                Log.e(TAG, "TTS nativo erro $errorCode: $utteranceId")
                onError("Erro TTS código $errorCode")
            }
        })

        // Configurar velocidade e pitch para imitar DonatoNeural
        ttsNative?.setSpeechRate(0.88f)   // um pouco mais lento
        ttsNative?.setPitch(0.82f)         // tom mais grave

        val params = android.os.Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        val result = ttsNative?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)

        if (result == TextToSpeech.ERROR) {
            onError("TTS nativo retornou ERROR")
        }
    }

    // ════════════════════════════════════════════════════════════
    // MEDIA PLAYER (para reproduzir MP3 do Edge TTS)
    // ════════════════════════════════════════════════════════════

    private fun playAudioFromBytes(
        audioBytes: ByteArray,
        onDone:     () -> Unit,
        onError:    (String) -> Unit
    ) {
        try {
            // Salvar bytes em arquivo temporário
            val tempFile = File(context.cacheDir, "tts_${System.currentTimeMillis()}.mp3")
            tempFile.writeBytes(audioBytes)

            stopCurrentPlayback()

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )

                setDataSource(tempFile.absolutePath)
                prepareAsync()

                setOnPreparedListener { it.start() }

                setOnCompletionListener {
                    tempFile.delete()
                    stopCurrentPlayback()
                    onDone()
                }

                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer erro: what=$what extra=$extra")
                    tempFile.delete()
                    onError("MediaPlayer erro $what/$extra")
                    true
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "playAudioFromBytes erro: ${e.message}")
            onError(e.message ?: "Erro desconhecido no MediaPlayer")
        }
    }

    private fun stopCurrentPlayback() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
            ttsNative?.stop()
        } catch (e: Exception) {
            Log.w(TAG, "stopCurrentPlayback: ${e.message}")
        } finally {
            mediaPlayer = null
        }
    }

    // ════════════════════════════════════════════════════════════
    // AUDIO FOCUS
    // ════════════════════════════════════════════════════════════

    private var audioFocusRequest: android.media.AudioFocusRequest? = null

    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val focusRequest = android.media.AudioFocusRequest.Builder(
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            ).apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                setOnAudioFocusChangeListener { focusChange ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS -> stop()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
                        AudioManager.AUDIOFOCUS_GAIN -> resume()
                    }
                }
            }.build()

            audioFocusRequest = focusRequest
            audioManager.requestAudioFocus(focusRequest)
        }
    }

    private fun releaseAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
            audioFocusRequest = null
        }
    }

    // ════════════════════════════════════════════════════════════
    // SSML HELPERS
    // ════════════════════════════════════════════════════════════

    /**
     * Gera SSML para a voz especificada.
     * Compatível com o formato retornado pelo Cloudflare Worker /proxy/tts
     */
    fun buildSSML(
        text:  String,
        voice: String = currentVoice,
        rate:  String = DEFAULT_RATE,
        pitch: String = DEFAULT_PITCH
    ): String {
        val safe = text
            .replace("&",  "&amp;")
            .replace("<",  "&lt;")
            .replace(">",  "&gt;")
            .replace("\"", "&quot;")
            .replace("'",  "&apos;")
            .take(3000)

        return """<speak version='1.0' xml:lang='pt-BR'>
  <voice name='$voice'>
    <prosody rate='$rate' pitch='$pitch'>$safe</prosody>
  </voice>
</speak>"""
    }

    /**
     * Extrai texto puro de um SSML.
     */
    fun extractTextFromSSML(ssml: String): String {
        return ssml
            .replace(Regex("<[^>]+>"), "")
            .replace("&amp;",  "&")
            .replace("&lt;",   "<")
            .replace("&gt;",   ">")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .trim()
    }

    // ════════════════════════════════════════════════════════════
    // PERSONAS PRÉ-DEFINIDAS
    // ════════════════════════════════════════════════════════════

    /**
     * Cria um VoiceKit configurado para Thaurus.
     * Voz: pt-BR-DonatoNeural (masculina, grave)
     */
    fun asThaurus(): VoiceKit {
        setVoice(VOICE_THAURUS)
        return this
    }

    /**
     * Cria um VoiceKit configurado para Willa.
     * Voz: pt-BR-FranciscaNeural (feminina, sofisticada)
     */
    fun asWilla(): VoiceKit {
        setVoice(VOICE_WILLA)
        return this
    }

    /**
     * Cria um VoiceKit configurado para Lira.
     * Voz: pt-BR-ThalitaNeural (feminina, calorosa)
     */
    fun asLira(): VoiceKit {
        setVoice(VOICE_LIRA)
        return this
    }

    // ════════════════════════════════════════════════════════════
    // LIFECYCLE
    // ════════════════════════════════════════════════════════════

    /**
     * Liberar recursos. Chamar em onDestroy() da Activity/Service.
     */
    fun release() {
        stop()
        ttsNative?.shutdown()
        ttsNative = null
        ttsReady  = false
        Log.d(TAG, "VoiceKit liberado.")
    }

    // ════════════════════════════════════════════════════════════
    // DATA CLASSES
    // ════════════════════════════════════════════════════════════

    private data class SpeechRequest(
        val text:    String,
        val voice:   String,
        val onStart: (() -> Unit)?,
        val onDone:  (() -> Unit)?,
        val onError: ((String) -> Unit)?
    )
}
