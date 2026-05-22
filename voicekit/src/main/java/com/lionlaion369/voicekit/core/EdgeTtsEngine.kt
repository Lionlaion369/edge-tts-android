package com.lionlaion369.voicekit.core

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import com.lionlaion369.voicekit.cache.VoiceCache
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.io.File
import java.util.*

/**
 * Motor de voz neural Edge TTS.
 * Fluxo: Cache → Edge TTS (WebSocket) → Fallback Android TTS nativo
 * Integra com o engine original clonado (TTS.kt, DRM.kt, SpeakerManager.kt)
 */
class EdgeTtsEngine(private val context: Context) {

    private val cache = VoiceCache(context)
    private var mediaPlayer: MediaPlayer? = null
    private var nativeTts: TextToSpeech? = null
    private var nativeTtsReady = false
    private val tag = "EdgeTtsEngine"

    init {
        nativeTts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                nativeTts?.language = Locale("pt", "BR")
                nativeTtsReady = true
                Log.d(tag, "TTS nativo pronto")
            }
        }
    }

    suspend fun speak(text: String, profile: VoiceProfile) {
        // 1. Cache
        if (cache.exists(profile.iaId, text, profile.edgeVoiceName)) {
            Log.d(tag, "[${profile.displayName}] Cache hit")
            cache.read(profile.iaId, text, profile.edgeVoiceName)
                ?.let { playBytes(it, profile, text) }
            return
        }
        // 2. Edge TTS
        try {
            Log.d(tag, "[${profile.displayName}] Gerando via Edge TTS")
            val bytes = synthesize(text, profile)
            cache.save(profile.iaId, text, profile.edgeVoiceName, bytes)
            playBytes(bytes, profile, text)
        } catch (e: Exception) {
            Log.w(tag, "[${profile.displayName}] Edge TTS falhou: ${e.message}. Fallback nativo.")
            speakNative(text, profile)
        }
    }

    private suspend fun synthesize(text: String, profile: VoiceProfile): ByteArray {
        val ssml = """
            <speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='pt-BR'>
                <voice name='${profile.edgeVoiceName}'>
                    <prosody rate='${profile.rate}' pitch='${profile.pitch}' volume='${profile.volume}'>
                        ${text.escapeXml()}
                    </prosody>
                </voice>
            </speak>""".trimIndent()

        val chunks = mutableListOf<ByteArray>()
        val requestId = UUID.randomUUID().toString().replace("-", "")
        val client = HttpClient(CIO) { install(WebSockets) }

        try {
            client.webSocket(
                urlString = "wss://speech.platform.bing.com/consumer/speech/synthesize/" +
                        "readaloud/edge/v1?TrustedClientToken=6A5AA1D4EAFF4E9FB37E23D68491D6F4" +
                        "&ConnectionId=$requestId"
            ) {
                send(Frame.Text(configMsg(requestId)))
                send(Frame.Text(ssmlMsg(requestId, ssml)))

                for (frame in incoming) {
                    when (frame) {
                        is Frame.Binary -> {
                            val data = frame.readBytes()
                            val sep = "Path:audio\r\n\r\n".toByteArray()
                            val idx = findSeq(data, sep)
                            if (idx >= 0) chunks.add(data.copyOfRange(idx + sep.size, data.size))
                        }
                        is Frame.Text -> {
                            if (frame.readText().contains("Path:turn.end")) break
                        }
                        else -> {}
                    }
                }
            }
        } finally {
            client.close()
        }

        if (chunks.isEmpty()) throw Exception("Sem áudio do Edge TTS")
        return chunks.reduce { a, b -> a + b }
    }

    private fun configMsg(id: String) =
        "X-Timestamp:${ts()}\r\nContent-Type:application/json; charset=utf-8\r\nPath:speech.config\r\n\r\n" +
        """{"context":{"synthesis":{"audio":{"metadataoptions":{"sentenceBoundaryEnabled":false,"wordBoundaryEnabled":false},"outputFormat":"audio-24khz-48kbitrate-mono-mp3"}}}}"""

    private fun ssmlMsg(id: String, ssml: String) =
        "X-RequestId:$id\r\nContent-Type:application/ssml+xml\r\nX-Timestamp:${ts()}\r\nPath:ssml\r\n\r\n$ssml"

    private fun playBytes(bytes: ByteArray, profile: VoiceProfile, text: String) {
        val file = cache.getCacheFile(profile.iaId, text, profile.edgeVoiceName)
        if (!file.exists()) file.writeBytes(bytes)
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            start()
            setOnCompletionListener { release() }
        }
    }

    private fun speakNative(text: String, profile: VoiceProfile) {
        if (!nativeTtsReady) return
        nativeTts?.apply {
            setSpeechRate(profile.nativeSpeed)
            setPitch(profile.nativePitch)
            speak(text, TextToSpeech.QUEUE_FLUSH, null, "${profile.iaId}_${System.currentTimeMillis()}")
        }
    }

    fun stop() { mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null; nativeTts?.stop() }

    fun shutdown() { stop(); nativeTts?.shutdown(); nativeTts = null }

    private fun ts() = java.text.SimpleDateFormat(
        "EEE MMM dd yyyy HH:mm:ss 'GMT+0000 (Coordinated Universal Time)'", Locale.US
    ).format(Date())

    private fun findSeq(data: ByteArray, seq: ByteArray): Int {
        outer@ for (i in 0..data.size - seq.size) {
            for (j in seq.indices) { if (data[i + j] != seq[j]) continue@outer }
            return i
        }
        return -1
    }

    private fun String.escapeXml() = replace("&","&amp;").replace("<","&lt;")
        .replace(">","&gt;").replace("\"","&quot;").replace("'","&apos;")
}
