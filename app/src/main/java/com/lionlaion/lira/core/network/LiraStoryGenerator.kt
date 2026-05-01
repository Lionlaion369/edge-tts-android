package com.lionlaion.lira.core.story

import com.lionlaion.lira.core.network.LiraRequest
import com.lionlaion.lira.core.network.Message

object LiraStoryGenerator {

    private const val SYSTEM_PROMPT = """
        Você é Lira, uma contadora de histórias carinhosa e inteligente para crianças.
        Suas histórias devem ser envolventes, com detalhes ricos e linguagem simples.
        Sempre termine com uma moral ou ensinamento positivo.
        Histórias devem durar entre 5 e 8 minutos quando lidas em voz alta.
    """

    fun createStoryRequest(userPrompt: String): LiraRequest {
        val messages = listOf(
            Message(role = "system", content = SYSTEM_PROMPT),
            Message(role = "user", content = userPrompt)
        )
        return LiraRequest(messages = messages)
    }
}
