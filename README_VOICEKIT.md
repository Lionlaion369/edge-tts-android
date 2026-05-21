# 🎙️ VoiceKit — Sistema de Vozes para Todas as IAs
> Repositório: `Lionlaion369/edge-tts-android`  
> Versão: 1.0.0 | Status: ✅ Pronto para Integração

---

## O QUE É ESTE REPOSITÓRIO

Este repositório contém **dois sistemas**:

| Pasta | O que é | Para quê |
|-------|---------|----------|
| `engine/` | Motor original (clonado de yynag) | Conecta com a API neural da Microsoft Edge via WebSocket |
| `voicekit/` | Camada de integração (criada por nós) | Perfis de voz de cada IA + cache + fallback |

---

## ESTRUTURA DE ARQUIVOS

```
Lionlaion369/edge-tts-android/
│
├── engine/                          ← NÃO ALTERE (código original clonado)
│   └── src/main/java/com/istomyang/tts_engine/
│       ├── TTS.kt
│       ├── SpeakerManager.kt
│       └── DRM.kt
│
├── voicekit/                        ← NOSSO SISTEMA (copie para integrar)
│   └── src/main/java/com/lionlaion369/voicekit/
│       ├── VoiceKit.kt              ← Ponto de entrada (use este)
│       ├── core/
│       │   ├── VoiceProfile.kt      ← Modelo de perfil de voz
│       │   └── EdgeTtsEngine.kt     ← Motor + cache + fallback
│       ├── profiles/
│       │   └── IaVoices.kt          ← Perfis de todas as IAs ← EDITE AQUI
│       ├── cache/
│       │   └── VoiceCache.kt        ← Cache de áudios
│       └── integration/
│           └── LiraVoiceIntegration.kt ← Exemplos de integração
│
├── settings.gradle.kts              ← Substitua pelo novo
└── README_VOICEKIT.md               ← Este arquivo
```

---

## COMO INTEGRAR EM CADA PROJETO DE IA

### ✅ LIRA (com.lira.app)

**1. No `settings.gradle.kts` da Lira:**
```kotlin
include(":voicekit")
project(":voicekit").projectDir = File("../edge-tts-android/voicekit")
```

**2. No `app/build.gradle.kts` da Lira:**
```kotlin
implementation(project(":voicekit"))
```

**3. No `LiraApplication.kt` (ou onde inicializa o app):**
```kotlin
import com.lionlaion369.voicekit.VoiceKit

class LiraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VoiceKit.init(this)  // ← adicione esta linha
    }
}
```

**4. Onde a Lira fala (substitua o TextToSpeech atual):**
```kotlin
import com.lionlaion369.voicekit.VoiceKit
import com.lionlaion369.voicekit.profiles.IaVoices

// Para respostas normais da Lira:
lifecycleScope.launch {
    VoiceKit.speak(IaVoices.LIRA, textoGerado)
}

// Para narrar história completa (7min33s):
lifecycleScope.launch {
    VoiceKit.speak(IaVoices.LIRA_STORY_MODE, historiaCompleta)
}
```

---

### ✅ WILLA

```kotlin
lifecycleScope.launch {
    VoiceKit.speak(IaVoices.WILLA, "Olá, William! Como posso ajudar?")
}
```

---

### ✅ AEGIS

```kotlin
lifecycleScope.launch {
    VoiceKit.speak(IaVoices.AEGIS, "Sistema protegido e monitorado.")
}
```

---

### ✅ THESAURUS

```kotlin
lifecycleScope.launch {
    VoiceKit.speak(IaVoices.THESAURUS, "A palavra significa...")
}
```

---

### ✅ AURORA

```kotlin
lifecycleScope.launch {
    VoiceKit.speak(IaVoices.AURORA, "Bom dia! Que dia lindo hoje!")
}
```

---

### ✅ NOVA IA (qualquer futura)

**1. Abra `voicekit/src/main/java/com/lionlaion369/voicekit/profiles/IaVoices.kt`**

**2. Copie este bloco e preencha:**
```kotlin
val NOME_DA_IA = VoiceProfile(
    iaId = "nome_da_ia",           // ID único, sem espaços
    displayName = "Nome da IA",
    gender = VoiceGender.FEMININE, // ou MASCULINE ou NEUTRAL
    edgeVoiceName = "pt-BR-ThalitaNeural", // escolha da lista abaixo
    rate = "+0%",    // velocidade: -50% a +50%
    pitch = "+0Hz",  // tom: -20Hz a +20Hz
    volume = "+0%",
    nativePitch = 1.0f,
    nativeSpeed = 1.0f,
    description = "Descrição da IA."
)
```

**3. Adicione no mapa `ALL_VOICES`:**
```kotlin
val ALL_VOICES: Map<String, VoiceProfile> = mapOf(
    // ... existentes ...
    NOME_DA_IA.iaId to NOME_DA_IA,  // ← adicione aqui
)
```

**4. Use no projeto:**
```kotlin
VoiceKit.speak(IaVoices.NOME_DA_IA, "Texto aqui")
```

---

## VOZES DISPONÍVEIS (PT-BR)

### Femininas
| Nome | Característica | Indicada para |
|------|---------------|---------------|
| `pt-BR-ThalitaNeural` | Jovem, calorosa, expressiva | Lira ✅ |
| `pt-BR-FranciscaNeural` | Madura, sofisticada | Willa ✅ |
| `pt-BR-BrendaNeural` | Jovem, vibrante | Aurora ✅ |
| `pt-BR-ElazaNeural` | Suave, delicada | IAs de bem-estar |
| `pt-BR-YaraNeural` | Natural, conversacional | Assistentes gerais |

### Masculinas
| Nome | Característica | Indicada para |
|------|---------------|---------------|
| `pt-BR-AntonioNeural` | Firme, natural | Aegis ✅ |
| `pt-BR-DonatoNeural` | Maduro, professoral | Thesaurus ✅ |
| `pt-BR-FabioNeural` | Jovem, casual | IAs informais |
| `pt-BR-NicolauNeural` | Maduro, formal | IAs corporativas |

---

## CACHE DE ÁUDIOS

O VoiceKit salva automaticamente todos os áudios gerados no celular:

```
/cache/voicekit/
  lira/      ← histórias da Lira ficam salvas aqui
  willa/     ← respostas da Willa
  aegis/     ← alertas do Aegis
  thesaurus/ ← explicações do Thesaurus
  aurora/    ← mensagens da Aurora
```

**Vantagem:** A segunda vez que a IA fala o mesmo texto, usa o cache (zero internet, zero custo, instantâneo).

---

## FALLBACK AUTOMÁTICO

Se a API da Microsoft Edge estiver fora do ar:
1. O VoiceKit detecta automaticamente
2. Usa o **TTS nativo do Android** com os mesmos parâmetros de voz
3. Qualidade menor, mas o app nunca para de funcionar

---

*VoiceKit — Lionlaion369/edge-tts-android — Maio 2026*
