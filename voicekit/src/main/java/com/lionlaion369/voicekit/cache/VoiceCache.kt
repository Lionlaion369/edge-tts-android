package com.lionlaion369.voicekit.cache

import java.io.File
import java.security.MessageDigest
import android.content.Context

/**
 * Cache de áudios gerados para todas as IAs.
 * Salva em: /cache/voicekit/{iaId}/{hash}.mp3
 * Segunda vez que a IA fala o mesmo texto = zero rede, zero custo.
 */
class VoiceCache(context: Context) {

    private val baseDir = File(context.cacheDir, "voicekit")

    init { baseDir.mkdirs() }

    fun getCacheFile(iaId: String, text: String, voiceName: String): File {
        val iaDir = File(baseDir, iaId).also { it.mkdirs() }
        return File(iaDir, "${md5("${voiceName}_${text}")}.mp3")
    }

    fun exists(iaId: String, text: String, voiceName: String): Boolean =
        getCacheFile(iaId, text, voiceName).exists()

    fun save(iaId: String, text: String, voiceName: String, audioBytes: ByteArray) =
        getCacheFile(iaId, text, voiceName).writeBytes(audioBytes)

    fun read(iaId: String, text: String, voiceName: String): ByteArray? =
        getCacheFile(iaId, text, voiceName).takeIf { it.exists() }?.readBytes()

    fun clearForIa(iaId: String) = File(baseDir, iaId).deleteRecursively()

    fun clearAll() { baseDir.deleteRecursively(); baseDir.mkdirs() }

    fun cacheSizeMb(iaId: String? = null): Float {
        val dir = if (iaId != null) File(baseDir, iaId) else baseDir
        return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() } / (1024f * 1024f)
    }

    private fun md5(input: String): String =
        MessageDigest.getInstance("MD5").digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
