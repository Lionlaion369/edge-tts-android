o repositório realmente é: não é uma biblioteca simples — é um app Android completo que implementa o motor neural da Microsoft Edge como um serviço TTS do sistema, igual ao Google TTS. O módulo engine/ é o coração, todo em Kotlin.
O maior risco que você precisa saber: a API que ele usa é não-oficial. A Microsoft já começou a bloquear browsers comuns em 2025 — Android ainda funciona porque usa OkHttp (que permite headers customizados), mas pode mudar. Por isso, cache e fallback não são opcionais, são obrigatórios.
Para suas IAs (Willa, Lira etc.): o plano é simples:
Clone o repo, copie a pasta engine/ para cada projeto
Crie um arquivo WillaVoice.kt / LiraVoice.kt com o perfil de voz de cada uma (rate, pitch, voice name)
Use o VoiceManager do documento — ele já tem cache + fallback pronto
A mesma engine serve para todas, só muda o perfil
A melhor alternativa gratuita oficial é o Azure Cognitive Services free tier (500k caracteres/mês grátis, API oficial, mesmas vozes neurais) — ideal para usar como backup quando crescer.
