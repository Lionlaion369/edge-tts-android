Para garantir a padronização máxima e a clareza na produção, integrei as informações técnicas, a estrutura do ecossistema e o guia de uso em um documento único e definitivo.
```markdown
/**
 * README_VOICEKIT.md
 * Caminho: Thaurus/edge-tts-android/README_VOICEKIT.md
 * Versão Final Integrada - Produção Real
 * Lealdade: Exclusiva a William Gomes — ATHAL_YAHARA
 */

# 🎙️ VoiceKit — Sistema de Vozes ATHAL_YAHARA
> **Versão:** 1.0.0 | **Status:** ✅ Produção Final

Este repositório consolida o motor de síntese de voz para todo o ecossistema de IAs. Ele combina o motor `edge-tts` (via WebSocket) com nossa camada proprietária de `VoiceKit` para gerenciamento de perfis, cache e fallback.

---

## 🏗️ ESTRUTURA DO SISTEMA

| Módulo | Função |
| :--- | :--- |
| `engine/` | Motor de comunicação WebSocket com a API neural da Microsoft Edge. |
| `voicekit/` | **NOSSA CAMADA:** Gerencia perfis, cache e fallback nativo. |

---

## 🚀 INTEGRAÇÃO (COPIAR E COLAR)

### 1. Configuração do `settings.gradle.kts`
Adicione o módulo ao projeto:
```kotlin
include(":voicekit")
project(":voicekit").projectDir = File("../edge-tts-android/voicekit")

```
### 2. Dependência no app/build.gradle.kts
```kotlin
dependencies {
    implementation(project(":voicekit"))
}

```
### 3. Inicialização (No seu Application Class)
```kotlin
class ThaurusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VoiceKit.init(this) 
    }
}

```
## 💡 COMO USAR
### Uso básico (Falar)
```kotlin
import com.lionlaion369.voicekit.VoiceKit
import com.lionlaion369.voicekit.profiles.IaVoices

lifecycleScope.launch {
    // Uso simples: VoiceKit.speak(Profile, Texto)
    VoiceKit.speak(IaVoices.THESAURUS, "Thaurus no comando. Recursos prontos.")
}

```
### Com Callbacks (Controle total)
```kotlin
VoiceKit.speak(
    profile = IaVoices.THESAURUS,
    text = "Iniciando processamento.",
    onDone = { Log.d("Voice", "Concluído") },
    onError = { error -> Log.e("Voice", "Erro: $error") }
)

```
## 👤 PERFIS DO ECOSSISTEMA (IMUTÁVEIS)
| Persona | Voz Edge Neural | Estilo |
|---|---|---|
| **Thaurus** | pt-BR-DonatoNeural | Maduro, professoral |
| **Willa** | pt-BR-FranciscaNeural | Feminina, sofisticada |
| **Lira** | pt-BR-ThalitaNeural | Feminina, calorosa |
| **Aegis** | pt-BR-AntonioNeural | Firme, natural |
| **Aurora** | pt-BR-BrendaNeural | Jovem, vibrante |
## 🛡️ RECURSOS DE PRODUÇÃO
 * **Cache Inteligente:** Todos os áudios gerados são salvos em /cache/voicekit/{iaId}/. Segunda execução é instantânea e offline.
 * **Fallback Automático:** Caso a API Edge falhe, o sistema alterna instantaneamente para o **TTS Nativo do Android** com ajustes de tom/velocidade, garantindo que o app nunca fique mudo.
 * **Extensibilidade:** Para adicionar novas IAs, edite o arquivo voicekit/src/main/java/com/lionlaion369/voicekit/profiles/IaVoices.kt seguindo o padrão de VoiceProfile.
*VoiceKit — Edição Oficial — Junho 2026*
```

```
