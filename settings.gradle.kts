/**
 * settings.gradle.kts
 * Caminho: Thaurus/settings.gradle.kts
 * Versão Final Integrada - Produção Real
 */

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "edge-tts-android"

// Módulos do sistema Thaurus
include(":app")
include(":voicekit")
include(":engine")
