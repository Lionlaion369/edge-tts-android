o repositório realmente é: não é uma biblioteca simples — é um app Android completo que implementa o motor neural da Microsoft Edge como um serviço TTS do sistema, igual ao Google TTS. O módulo engine/ é o coração, todo em Kotlin.
O maior risco que você precisa saber: a API que ele usa é não-oficial. A Microsoft já começou a bloquear browsers comuns em 2025 — Android ainda funciona porque usa OkHttp (que permite headers customizados), mas pode mudar. Por isso, cache e fallback não são opcionais, são obrigatórios.
Para suas IAs (Willa, Lira etc.): o plano é simples:
Clone o repo, copie a pasta engine/ para cada projeto
Crie um arquivo WillaVoice.kt / LiraVoice.kt com o perfil de voz de cada uma (rate, pitch, voice name)
Use o VoiceManager do documento — ele já tem cache + fallback pronto
A mesma engine serve para todas, só muda o perfil
A melhor alternativa gratuita oficial é o Azure Cognitive Services free tier (500k caracteres/mês grátis, API oficial, mesmas vozes neurais) — ideal para usar como backup quando crescer.
# 🎙️ Análise Completa: edge-tts-android + Plano de Voz para Willa, Lira e suas IAs

---

## 📦 O QUE É O REPOSITÓRIO `yynag/edge-tts-android`

### Resumo em uma frase
É um **app Android completo** (não uma biblioteca) que implementa o motor de voz neural da Microsoft Edge como um **serviço TTS do sistema Android**.

### Estrutura real do repositório
```
edge-tts-android/
├── engine/          ← O CORAÇÃO: módulo Kotlin com toda a lógica TTS
├── app/             ← App de demonstração/configuração (UI)
├── .github/         ← CI/CD automático (GitHub Actions)
├── gradle/          ← Build tools
└── docs/images/     ← Screenshots
```

### O que o módulo `engine/` faz (o mais importante)
1. **Conecta via WebSocket** ao serviço neural da Microsoft Edge
2. **Envia SSML** (Speech Synthesis Markup Language) com voz, pitch, rate
3. **Recebe áudio MP3** em streaming de alta qualidade
4. **Implementa a interface `TextToSpeechService`** do Android — isso significa que ele se registra como um motor TTS do sistema operacional, igual ao Google TTS
5. **Gerencia sessões, tokens e reconexão** automática

### Versão mais recente
- v1.5.3 (Janeiro 2025) — última correção: "using playing audio app instead of converting to PCM"

---

## ⚠️ ANÁLISE DE FALHAS E RISCOS REAIS

### Problema crítico: A API da Microsoft pode quebrar a qualquer momento
O repositório usa a **API não-oficial** do serviço de leitura do Microsoft Edge ("Read Aloud"). Isso significa:

| Risco | Nível | Detalhe |
|-------|-------|---------|
| Microsoft pode bloquear | 🔴 Alto | Já aconteceu com browsers — agora exige header customizado `Sec-WebSocket-Version` que só o Edge browser pode enviar |
| Não é para uso comercial | 🔴 Alto | Termos de uso da Microsoft proíbem uso fora do Edge |
| Pode cair sem aviso | 🟡 Médio | Histórico de instabilidades ocasionais |
| Requer internet sempre | 🟡 Médio | Sem modo offline real |

### O que já quebrou em browsers
Em 2025, a Microsoft passou a exigir um header WebSocket customizado que browsers comuns (Chrome, Firefox) não podem enviar. **Android usa OkHttp, que pode enviar headers customizados** — por isso o app ainda funciona, mas é um sinal de que a Microsoft está restringindo.

### O que está OK no repositório
- ✅ Código em Kotlin puro, bem estruturado
- ✅ 116 estrelas, 11 forks, ativamente mantido até jan/2025
- ✅ Implementa interface nativa do Android (baixo risco de incompatibilidade)
- ✅ Fallback possível para TTS nativo
- ✅ Vozes brasileiras disponíveis (pt-BR-FranciscaNeural, pt-BR-AntonioNeural)

---

## 🎯 RESPOSTA DIRETA: É PRONTO PARA PRODUÇÃO?

**Para uso pessoal e projetos pequenos: SIM, funciona bem.**

**Para um produto comercial sério (Lira, Willa): NÃO sem um plano B.**

O risco real não é o código em si — é depender de uma API não-oficial que pode ser cortada a qualquer momento pela Microsoft.

---

## 🏗️ PLANO: REPOSITÓRIO DE VOZ PRÓPRIO (PARA TODAS SUAS IAs)

### Arquitetura recomendada: "VoiceKit"

```
SeuOrg/voicekit-android/
├── core/                    ← Módulo principal (copie para qualquer projeto)
│   ├── VoiceEngine.kt       ← Interface abstrata (não depende de nenhum provider)
│   ├── EdgeTtsProvider.kt   ← Implementação Edge (usa o engine do yynag)
│   ├── NativeTtsProvider.kt ← Fallback Android nativo
│   └── VoiceCache.kt        ← Cache local de áudios gerados
├── voices/                  ← Configuração de cada IA
│   ├── WillaVoice.kt        ← Voz da Willa (configuração específica)
│   ├── LiraVoice.kt         ← Voz da Lira
│   └── VoiceProfile.kt      ← Modelo de perfil de voz
└── sample-app/              ← App de teste
```

---

## 📋 CÓDIGO PRONTO PARA COPIAR E COLAR

### 1. Interface Principal (cole em qualquer projeto)

```kotlin
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
```

### 2. Perfis de Voz para Willa e Lira

```kotlin
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
```

### 3. Manager com Cache e Fallback (o mais importante)

```kotlin
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
```

### 4. Alternativa via Cloudflare Worker (mais segura para produção)

```javascript
// cloudflare-worker-tts.js — Deploy no seu Worker existente
// Adicione esta rota ao lira.lionlaion369.workers.dev

export default {
  async fetch(request) {
    const url = new URL(request.url);
    
    if (url.pathname === '/tts') {
      const body = await request.json();
      const { text, voice = 'pt-BR-FranciscaNeural', rate = '+0%', pitch = '+0Hz' } = body;
      
      // Constrói SSML
      const ssml = `
        <speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='pt-BR'>
          <voice name='${voice}'>
            <prosody rate='${rate}' pitch='${pitch}'>
              ${text}
            </prosody>
          </voice>
        </speak>`;
      
      // Chama Azure Cognitive (free tier: 500k chars/mês)
      // OU redireciona para o motor edge-tts via servidor Node.js
      
      return new Response(JSON.stringify({ ssml, voice }), {
        headers: { 'Content-Type': 'application/json' }
      });
    }
  }
}
```

---

## 🆓 OPÇÕES GRATUITAS REAIS (DA MELHOR PARA PIOR)

| Opção | Qualidade | Limite | Risco | Recomendação |
|-------|-----------|--------|-------|--------------|
| **Edge TTS (via yynag)** | ⭐⭐⭐⭐⭐ | Ilimitado* | 🔴 API não-oficial | Usar + cache agressivo |
| **Azure Cognitive Free** | ⭐⭐⭐⭐⭐ | 500k chars/mês | 🟢 Oficial | Melhor para produção |
| **Google Cloud TTS Free** | ⭐⭐⭐⭐ | 1M chars/mês | 🟢 Oficial | Boa alternativa |
| **Android TTS Nativo** | ⭐⭐⭐ | Ilimitado | 🟢 Zero risco | Fallback obrigatório |
| **Kokoro TTS (local)** | ⭐⭐⭐⭐ | Ilimitado offline | 🟢 Zero risco | Futuro (requer hardware) |

*Ilimitado mas não-oficial — pode ser bloqueado

---

## 🚀 PLANO DE AÇÃO PARA VOCÊ AGORA

### Fase 1 — Lançamento (30 dias)
1. Clone o `yynag/edge-tts-android`
2. Copie o módulo `engine/` para o projeto da Lira
3. Use o `VoiceManager` acima com `LiraVoice.profile`
4. **Cache TUDO**: gere as 30 histórias em áudio antes do lançamento
5. Fallback: Android TTS nativo para histórias não cacheadas

### Fase 2 — Estabilidade (60-90 dias)
1. Cadastre no Azure Free Tier (500k chars/mês grátis, oficial)
2. Use Edge TTS como primário + Azure como backup
3. Adicione o `VoiceKit` ao projeto da Willa com `WillaVoice.profile`

### Fase 3 — Escala
1. Cache inteligente: histórias mais tocadas ficam sempre em cache
2. Pre-geração em background quando app abre com WiFi
3. Cada nova IA: crie um novo `XVoice.kt` com o perfil dela

---

## ✅ RESPOSTA FINAL: O QUE FAZER COM O REPOSITÓRIO

**Para copiar e usar agora:**
1. Clone o repo completo
2. Copie a pasta `engine/` para dentro do seu projeto como módulo
3. No `settings.gradle.kts` adicione: `include(":engine")`
4. No `build.gradle.kts` do app: `implementation(project(":engine"))`
5. Use o `VoiceManager` deste documento para chamar o engine

**O que adaptar para cada IA:**
- Crie um arquivo `[NomeDaIA]Voice.kt` com o `VoiceProfile` configurado
- Instancie o `VoiceManager` passando o profile correspondente
- A mesma engine serve para Willa, Lira, e qualquer outra IA futura

**O que NÃO fazer:**
- Não use sem cache (vai ser lento + risco de limite)
- Não lance sem fallback nativo (Android TTS)
- Não dependa 100% sem ter o Azure Free como backup

---

*Documento gerado para o projeto William — Willa/Eve · Maio 2026*
