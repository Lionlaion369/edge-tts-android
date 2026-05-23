# 🎙️ DOCUMENTO MESTRE — INTEGRAÇÃO VOICEKIT
## Ecossistema Lionlaion369 × edge-tts-android
> Versão: 1.0.0 | Data: Maio 2026 | Status: ✅ Pronto para execução

---

## 🗺️ VISÃO GERAL DO SISTEMA

```
edge-tts-android/              ← REPOSITÓRIO CENTRAL DE VOZES
├── engine/                    ← Motor Edge TTS (NÃO ALTERE)
│   └── TTS.kt, SpeakerManager.kt, DRM.kt
└── voicekit/                  ← NOSSO SISTEMA (copie para integrar)
    ├── VoiceKit.kt            ← Ponto de entrada único
    ├── core/
    │   ├── VoiceProfile.kt    ← Modelo de dados
    │   └── EdgeTtsEngine.kt   ← Motor + cache + fallback
    ├── profiles/
    │   └── IaVoices.kt        ← ← ← EDITE AQUI para novas IAs
    ├── cache/
    │   └── VoiceCache.kt      ← Cache automático de áudios
    └── integration/
        └── LiraVoiceIntegration.kt ← Exemplos

lira/                          ← App Lira (100% Kotlin)
Willa/                         ← App Willa (Kotlin + Capacitor)
Thaurus/                       ← App Thaurus (Multi-plataforma)
```

---

## 🎭 MAPA DE VOZES — CADA IA SUA VOZ ÚNICA

| IA | Gênero | Voz Neural | Perfil | Personalidade |
|----|--------|------------|--------|---------------|
| **Lira** | 👩 Feminina | `pt-BR-ThalitaNeural` | Jovem, calorosa | Tia contadora de histórias |
| **Lira Story** | 👩 Feminina | `pt-BR-ThalitaNeural` | Mais lenta, expressiva | Modo narração |
| **Willa** | 👩 Feminina | `pt-BR-FranciscaNeural` | Madura, sofisticada | Coordenadora central |
| **Willa Command** | 👩 Feminina | `pt-BR-FranciscaNeural` | Mais grave, assertiva | Modo autoridade |
| **Thaurus** | 👨 Masculina | `pt-BR-DonatoNeural` | Grave, maduro | Pilar da Economia |
| **Thaurus Activation** | 👨 Masculina | `pt-BR-DonatoNeural` | Máximo grave | Saudação dramática |
| **Thaurus Report** | 👨 Masculina | `pt-BR-DonatoNeural` | Natural, claro | Relatórios financeiros |

---

## 📦 SEQUÊNCIA DE INSTALAÇÃO (FAÇA NESTA ORDEM)

### Pré-requisito único:
```bash
# Na pasta pai onde ficam todos os projetos:
git clone https://github.com/Lionlaion369/edge-tts-android

# Estrutura final em disco:
# projetos/
#   ├── edge-tts-android/   ← clonado agora
#   ├── lira/
#   ├── Willa/
#   └── Thaurus/
```

### Ordem de integração:

**1. Atualize `IaVoices.kt` no edge-tts-android**
- Arquivo: `edge-tts-android/voicekit/src/main/java/com/lionlaion369/voicekit/profiles/IaVoices.kt`
- Ação: Substitua pelo arquivo `IaVoices_COMPLETO.kt` que está nesta pasta

**2. Integre a LIRA**
- Siga: `LIRA_VOICEKIT_INTEGRATION.md`
- Estimativa: 10 minutos

**3. Integre a WILLA**
- Siga: `WILLA_VOICEKIT_INTEGRATION.md`
- Estimativa: 15 minutos (tem mais módulos)

**4. Integre o THAURUS**
- Siga: `THAURUS_VOICEKIT_INTEGRATION.md`
- Estimativa: 15 minutos

---

## 🔧 A ÚNICA CHAMADA QUE VOCÊ PRECISA SABER

```kotlin
// Em QUALQUER lugar de QUALQUER projeto:
lifecycleScope.launch {
    VoiceKit.speak(IaVoices.NOME_DA_IA, "Texto que a IA vai falar")
}

// Para parar:
VoiceKit.stop()

// Para inicializar (só uma vez, no Application):
VoiceKit.init(this)
```

---

## 🆕 COMO ADICIONAR UMA IA NOVA NO FUTURO

1. Abra `edge-tts-android/voicekit/src/main/java/com/lionlaion369/voicekit/profiles/IaVoices.kt`

2. Copie e cole este bloco, preenchendo os valores:

```kotlin
val NOME_DA_IA = VoiceProfile(
    iaId = "nome_da_ia",            // ID único, sem espaços, minúsculo
    displayName = "Nome da IA",
    gender = VoiceGender.FEMININE,  // ou MASCULINE ou NEUTRAL
    edgeVoiceName = "pt-BR-ThalitaNeural", // escolha da tabela abaixo
    rate = "+0%",    // velocidade: -50% (lento) a +50% (rápido)
    pitch = "+0Hz",  // tom: -20Hz (grave) a +20Hz (agudo)
    volume = "+0%",  // volume: -50% a +50%
    nativePitch = 1.0f,   // fallback nativo: 0.5-2.0 (1.0 = normal)
    nativeSpeed = 1.0f,   // fallback nativo: 0.5-2.0 (1.0 = normal)
    description = "Descrição da IA e sua personalidade."
)
```

3. Adicione no mapa `ALL_VOICES`:
```kotlin
NOME_DA_IA.iaId to NOME_DA_IA,
```

4. Use no projeto:
```kotlin
VoiceKit.speak(IaVoices.NOME_DA_IA, "Texto aqui")
```

---

## 📱 VOZES DISPONÍVEIS PT-BR (Referência Completa)

### Femininas
| Voz | Característica | Indicada para |
|-----|---------------|---------------|
| `pt-BR-ThalitaNeural` | Jovem, calorosa, expressiva | **Lira** ✅ |
| `pt-BR-FranciscaNeural` | Madura, sofisticada | **Willa** ✅ |
| `pt-BR-BrendaNeural` | Jovem, vibrante | IAs energéticas |
| `pt-BR-ElazaNeural` | Suave, delicada | IAs de bem-estar |
| `pt-BR-YaraNeural` | Natural, conversacional | Assistentes gerais |

### Masculinas
| Voz | Característica | Indicada para |
|-----|---------------|---------------|
| `pt-BR-DonatoNeural` | Maduro, professoral, grave | **Thaurus** ✅ |
| `pt-BR-AntonioNeural` | Firme, natural | IAs de segurança |
| `pt-BR-FabioNeural` | Jovem, casual | IAs descontraídas |
| `pt-BR-NicolauNeural` | Maduro, formal | IAs corporativas |

---

## 💾 SISTEMA DE CACHE AUTOMÁTICO

O VoiceKit salva os áudios automaticamente:

```
/cache/voicekit/
  lira/       ← histórias da Lira salvas aqui (zero internet = zero custo)
  willa/      ← respostas da Willa
  thaurus/    ← análises do Thaurus
```

Segunda vez que a IA fala o mesmo texto → **usa cache, zero requisição, zero custo.**

---

## 🛡️ FALLBACK AUTOMÁTICO

Se a API Microsoft Edge estiver offline:
1. VoiceKit detecta automaticamente
2. Usa TTS nativo do Android com parâmetros similares
3. App nunca para de funcionar

---

## 📊 STATUS DOS MÓDULOS

| Módulo | Status | Arquivo Principal |
|--------|--------|------------------|
| engine/ (TTS core) | ✅ Pronto (não altere) | `engine/TTS.kt` |
| voicekit/VoiceKit.kt | ✅ Pronto | ponto de entrada |
| voicekit/IaVoices.kt | ⚙️ Atualizar com este doc | perfis das IAs |
| Lira — integração | ⏳ Pendente | `LIRA_VOICEKIT_INTEGRATION.md` |
| Willa — integração | ⏳ Pendente | `WILLA_VOICEKIT_INTEGRATION.md` |
| Thaurus — integração | ⏳ Pendente | `THAURUS_VOICEKIT_INTEGRATION.md` |

---

## 🚨 ERROS MAIS COMUNS E SOLUÇÕES

| Erro | Causa | Solução |
|------|-------|---------|
| `Project ':voicekit' not found` | Pasta errada | Confirme que `edge-tts-android` está ao lado (não dentro) do projeto |
| `Unresolved reference: VoiceKit` | Faltou dependência | Adicione `implementation(project(":voicekit"))` no `build.gradle.kts` |
| `Unresolved reference: IaVoices.LIRA` | Faltou import | Adicione `import com.lionlaion369.voicekit.profiles.IaVoices` |
| Sem som no celular | VoiceKit não iniciado | Confirme `VoiceKit.init(this)` no `Application.onCreate()` |
| Som com qualidade ruim | Usando fallback nativo | Verifique internet — EdgeTTS precisa de conexão na primeira vez |
| Build falha com Capacitor (Willa) | Conflito de módulos | Confirme versão do Capacitor no settings.gradle.kt |

---

## 🗓️ PRÓXIMOS PASSOS APÓS VOZES

Depois que as vozes estiverem funcionando, a ordem sugerida é:

1. **Visão** — integrar câmera/processamento de imagem
2. **Audição** — integrar reconhecimento de voz (Speech-to-Text)
3. **Memória persistente** — Room Database para cada IA
4. **Sincronização** — todas as IAs se comunicando via Willa (central)

---

*Documento Mestre VoiceKit — Lionlaion369 — Maio 2026*
*Para qualquer IA futura que precisar continuar este trabalho: leia este documento primeiro.*
