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

rootProject.name = "EdgeTSS"

// Módulos originais — NÃO alterar
include(":app")
include(":engine")

// VoiceKit — sistema de vozes para Lira, Willa, Aegis, Thesaurus, Aurora e futuras IAs
include(":voicekit")
