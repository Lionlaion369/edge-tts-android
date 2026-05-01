package com.lionlaion.lira.core.network

data class LiraRequest(
    val model: String = "grok-beta",
    val messages: List<Message>,
    val temperature: Double = 0.85,
    val max_tokens: Int = 800
)

data class Message(
    val role: String,
    val content: String
)
