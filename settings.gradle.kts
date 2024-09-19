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
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

val exRepositories = file("external-plugins/")
if (exRepositories.exists()) {
    exRepositories.list()?.forEach {
        include(it)
        project(":$it").projectDir = file("external-plugins/$it")
    }
}

rootProject.name = "test-moq-android"
include(":app")


